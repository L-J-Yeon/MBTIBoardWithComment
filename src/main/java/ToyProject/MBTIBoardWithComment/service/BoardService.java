package ToyProject.MBTIBoardWithComment.service;

import ToyProject.MBTIBoardWithComment.domain.Board;
import ToyProject.MBTIBoardWithComment.domain.BoardPage;
import ToyProject.MBTIBoardWithComment.domain.Comment;
import ToyProject.MBTIBoardWithComment.domain.Member;
import ToyProject.MBTIBoardWithComment.exception.DataNotFoundException;
import ToyProject.MBTIBoardWithComment.repository.BoardPageRepository;
import ToyProject.MBTIBoardWithComment.repository.BoardRepository;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardPageRepository boardPageRepository;

    private Specification<Board> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Board> b, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Board, Member> u1 = b.join("author", JoinType.LEFT);
                Join<Board, Comment> a = b.join("commentList", JoinType.LEFT);
                Join<Comment, Member> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(b.get("subject"), "%" + kw + "%"), // 제목
                        cb.like(b.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
            }
        };
    }

    public Page<Board> getList(int page, String kw) { //kw추가
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Board> spec = search(kw); //추가
        return this.boardRepository.findAll(spec, pageable); //spec추가
    }

    public Board getBoard(Integer id){
        Optional<Board> board = this.boardRepository.findById(id);
        if(board.isPresent()){
            //조회수
            Board board1 = board.get();
            board1.setCountview(board1.getCountview() + 1);
            this.boardRepository.save(board1);
            return board1;
            //조회수끝
        }else{
            throw new DataNotFoundException("question not found");
        }
    }

//    public Page<Board> getList(int page){
//        List<Sort.Order> sorts = new ArrayList<>();
//        sorts.add(Sort.Order.desc("createDate"));
//        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
//        return this.boardRepository.findAll(pageable);
//    }

    public void create(String subject, String content, Member member, String category){
        Board b = new Board();
        b.setSubject(subject);
        b.setContent(content);
        b.setCreateDate(new Date()); //오라클은.now()가 아닌 new Date()로 해주면 된다
        b.setAuthor(member);
        b.setCategory(category);
        this.boardRepository.save(b);
    }

    public void modify(Board board, String subject, String content, String category) {
        board.setSubject(subject);
        board.setContent(content);
        board.setModifyDate(new Date());
        board.setCategory(category);
        this.boardRepository.save(board);
    }

    public void delete(Board board){
        this.boardRepository.delete(board);
    }

    public void vote(Board board, Member member) {
        board.getVoter().add(member);
        this.boardRepository.save(board);
    }

    public BoardPage getBoardByPageId(Board board){
        //레파지토리에 작성해둔 findByPages 메서드에서 question엔티티의 id를 기준으로 쿼리문을 실행
        return boardPageRepository.findByPages(board.getId());
    }
}

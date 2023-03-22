package ToyProject.MBTIBoardWithComment.service;

import ToyProject.MBTIBoardWithComment.domain.Board;
import ToyProject.MBTIBoardWithComment.domain.Comment;
import ToyProject.MBTIBoardWithComment.domain.Member;
import ToyProject.MBTIBoardWithComment.exception.DataNotFoundException;
import ToyProject.MBTIBoardWithComment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;


    public Comment create(Board board, String content, Member author) {
        Comment comment = new Comment();

        comment.setContent(content);
        comment.setCreateDate(new Date());
        comment.setBoard(board);
        comment.setAuthor(author);

        this.commentRepository.save(comment);

        return comment;
    }

    /*답변조회*/
    public Comment getComment(Integer id){
        Optional<Comment> answer = this.commentRepository.findById(id);
        if(answer.isPresent()){
            return answer.get();
        }else{
            throw new DataNotFoundException("answer not found");
        }
    }

    /*답변수정*/
    public void modify(Comment comment, String content){
        comment.setContent(content);
        comment.setModifyDate(new Date());
        this.commentRepository.save(comment);
    }

    public void delete(Comment comment){
        this.commentRepository.delete(comment);
    }

    public void vote(Comment comment, Member member){
        comment.getVoter().add(member);
        this.commentRepository.save(comment);
    }

    /*페이징*/
    public Page<Comment> getList(Board board, int page){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));

        Pageable pageable = PageRequest.of(page, 5);
        return this.commentRepository.findAllByBoard(board, pageable);
    }
}

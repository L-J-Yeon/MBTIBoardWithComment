package ToyProject.MBTIBoardWithComment.service;

import ToyProject.MBTIBoardWithComment.domain.Board;
import ToyProject.MBTIBoardWithComment.domain.Comment;
import ToyProject.MBTIBoardWithComment.domain.Member;
import ToyProject.MBTIBoardWithComment.exception.DataNotFoundException;
import ToyProject.MBTIBoardWithComment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
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
}

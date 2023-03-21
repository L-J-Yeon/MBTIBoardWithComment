package ToyProject.MBTIBoardWithComment.service;

import ToyProject.MBTIBoardWithComment.domain.Board;
import ToyProject.MBTIBoardWithComment.domain.Comment;
import ToyProject.MBTIBoardWithComment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;


    public void create(Board board, String content) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCreateDate(new Date());  //now함수가아닌 newDate()
        comment.setBoard(board);
        this.commentRepository.save(comment);
    }
}

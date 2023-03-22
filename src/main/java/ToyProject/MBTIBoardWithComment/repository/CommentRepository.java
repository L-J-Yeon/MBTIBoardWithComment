package ToyProject.MBTIBoardWithComment.repository;

import ToyProject.MBTIBoardWithComment.domain.Board;
import ToyProject.MBTIBoardWithComment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Page<Comment> findAllByBoard(Board board, Pageable pageable);
}

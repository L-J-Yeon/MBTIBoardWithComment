package ToyProject.MBTIBoardWithComment.repository;

import ToyProject.MBTIBoardWithComment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}

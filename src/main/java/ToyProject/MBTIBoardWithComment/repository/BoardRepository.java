package ToyProject.MBTIBoardWithComment.repository;

import ToyProject.MBTIBoardWithComment.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    Board findBySubject(String subject);
    Board findBySubjectAndContent(String subject, String content);
    List<Board> findBySubjectLike(String subject);
    /*Page<Board> findAll(Pageable pageable);*/

    @Query("SELECT b from Board b ORDER BY b.id DESC")
    Page<Board> findAll(Specification<Board> spec, Pageable pageable);
}

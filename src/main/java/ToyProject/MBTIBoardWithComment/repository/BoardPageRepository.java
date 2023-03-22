package ToyProject.MBTIBoardWithComment.repository;

import ToyProject.MBTIBoardWithComment.domain.BoardPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardPageRepository extends JpaRepository<BoardPage, Integer> {
    @Query(value = "SELECT * FROM(SELECT ID, " +
            "LAG(ID, 1, 0) OVER(ORDER BY ID ASC) AS PREVID, " +
            "LAG(subject, 1, '이전글이 없습니다.') OVER (ORDER BY ID ASC) AS PREV_SUB," +
            "LEAD(ID, 1, 0) OVER(ORDER BY ID ASC) AS NEXTID, "+
            "LEAD(subject, 1, '다음글이 없습니다') OVER (ORDER BY ID ASC) AS NEXT_SUB " +
            "FROM QUESTION) WHERE id = :id",
            nativeQuery = true) //오라클쿼리문그대로 쓰겠다

    BoardPage findByPages(Integer id);
}

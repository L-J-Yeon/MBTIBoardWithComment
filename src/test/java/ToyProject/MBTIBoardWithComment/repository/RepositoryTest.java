package ToyProject.MBTIBoardWithComment.repository;

import ToyProject.MBTIBoardWithComment.domain.Board;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
public class RepositoryTest {
    @Autowired
    private BoardRepository boardRepository;

    @Test
    @DisplayName("데이터 저장 테스트")
    public void saveDataTest() {
        Board board1 = new Board();
        board1.setTitle("title1");
        board1.setContent("content1");
        board1.setCreatedDate(new Date());
        this.boardRepository.save(board1);

        Board board2 = new Board();
        board2.setTitle("title2");
        board2.setContent("content2");
        board2.setCreatedDate(new Date());
        this.boardRepository.save(board2);
    }
}

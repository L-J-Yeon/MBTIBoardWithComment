package ToyProject.MBTIBoardWithComment.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class BoardPage {
    @Id
    private Integer id;

    private String PREVID;
    private String PREV_SUB;
    private String NEXTID;
    private String NEXT_SUB;
}

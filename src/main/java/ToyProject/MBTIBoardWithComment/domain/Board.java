package ToyProject.MBTIBoardWithComment.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String subject;

    private String content;

    @Column(insertable = false, updatable = false, columnDefinition = "date default LOCALTIMESTAMP")
    private Date createDate;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int countview; /*조회수*/

    private String category;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Comment>commentList; //질문엔티티에 답변엔티티참조 (질문1:답변N)

    @ManyToOne //여러개의 질문이 한명의 사용자에게 작성될수있으므로 @ManyToOne
    private Member author; /*author 속성추가*/

    @Column(insertable = true, updatable = true, columnDefinition = "date default LOCALTIMESTAMP")
    private Date modifyDate;

    @ManyToMany //하나의 질문에 여러사람이 추천, 한사람이 여러개의 질문을 추천(대등관계)
    Set<Member> voter; /*추천인은 중복되면 안됨-Set*/
}

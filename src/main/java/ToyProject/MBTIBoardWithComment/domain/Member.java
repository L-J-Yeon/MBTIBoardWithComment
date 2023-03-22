package ToyProject.MBTIBoardWithComment.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    private String role;

    @PrePersist
    public void setting() {
        this.role = "ROLE_USER";
    }

    @Column(unique = true)
    private String nickname;
}

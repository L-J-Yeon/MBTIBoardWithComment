package ToyProject.MBTIBoardWithComment.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BoardDto {
    @NotEmpty(message = "제목은 필수항목입니다.") /*Null허용치않음*/
    @Size(max=200) /*최대길이200byte*/
    private String subject;

    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;
}

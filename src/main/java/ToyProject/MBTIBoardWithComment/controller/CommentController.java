package ToyProject.MBTIBoardWithComment.controller;

import ToyProject.MBTIBoardWithComment.domain.Board;
import ToyProject.MBTIBoardWithComment.dto.CommentDto;
import ToyProject.MBTIBoardWithComment.service.BoardService;
import ToyProject.MBTIBoardWithComment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@RequestMapping("/comment")
public class CommentController {
    private final BoardService boardService;
    private final CommentService commentService;

    @PostMapping("/create/{id}")/*AnswerForm을 사용하도록 컨트롤러 변경*/
    public String createComment(Model model, @PathVariable("id") Integer id,
                               @Valid CommentDto commentDto, BindingResult bindingResult) {
        Board board = this.boardService.getBoard(id);
        if(bindingResult.hasErrors()){
            model.addAttribute("board", board);
            return "board/boardDetail";
        }
        this.commentService.create(board, commentDto.getContent());
        return String.format("redirect:/board/detail/%s", id);
    }
}

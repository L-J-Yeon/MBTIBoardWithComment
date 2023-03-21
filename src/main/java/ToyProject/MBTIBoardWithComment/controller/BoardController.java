package ToyProject.MBTIBoardWithComment.controller;

import ToyProject.MBTIBoardWithComment.domain.Board;
import ToyProject.MBTIBoardWithComment.dto.BoardDto;
import ToyProject.MBTIBoardWithComment.dto.CommentDto;
import ToyProject.MBTIBoardWithComment.repository.BoardRepository;
import ToyProject.MBTIBoardWithComment.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    @RequestMapping("/list")
    public String list(Model model,
                       @RequestParam(value="page", defaultValue="0") int page){
        Page<Board> paging = this.boardService.getList(page);
        model.addAttribute("paging", paging); /*페이징*/
        return "board/boardList";
    }

    @RequestMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id,
                         CommentDto commentDto) { /*AnswerForm추가*/
        Board board = this.boardService.getBoard(id);
        model.addAttribute("board", board);
        return "board/boardDetail";
    }

    @GetMapping("/create") /*매개변수로 QuestionForm 객체를 추가*/
    public String boardCreate(BoardDto boardDto) {
        return "board/boardForm";
    }

    @PostMapping("/create") /*QuestionForm 사용할수있게 수정*/
    public String boardCreate(@Valid BoardDto boardDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "board/boardForm";
        }
        this.boardService.create(boardDto.getSubject(), boardDto.getContent());
        return "redirect:/board/list";
    }
}

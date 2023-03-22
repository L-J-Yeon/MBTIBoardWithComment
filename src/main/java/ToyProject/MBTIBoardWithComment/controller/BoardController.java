package ToyProject.MBTIBoardWithComment.controller;

import ToyProject.MBTIBoardWithComment.domain.Board;
import ToyProject.MBTIBoardWithComment.domain.Member;
import ToyProject.MBTIBoardWithComment.dto.BoardDto;
import ToyProject.MBTIBoardWithComment.dto.CommentDto;
import ToyProject.MBTIBoardWithComment.repository.BoardRepository;
import ToyProject.MBTIBoardWithComment.service.BoardService;
import ToyProject.MBTIBoardWithComment.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;
    private final MemberService memberService;

    @RequestMapping("/list")
    public String list(Model model,
                       @RequestParam(value="page", defaultValue="0") int page){
        Page<Board> paging = this.boardService.getList(page);
        model.addAttribute("paging", paging); /*페이징*/
        return "boardList";
    }

    @RequestMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id,
                         CommentDto commentDto) { /*AnswerForm추가*/
        Board board = this.boardService.getBoard(id);
        model.addAttribute("board", board);
        return "boardDetail";
    }

    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")/*매개변수로 QuestionForm 객체를 추가*/
    public String boardCreate(BoardDto boardDto) {
        return "boardForm";
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")/*QuestionForm 사용할수있게 수정*/
    public String boardCreate(@Valid BoardDto boardDto, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "boardForm";
        }
        Member member = this.memberService.getUser(principal.getName());
        this.boardService.create(boardDto.getSubject(), boardDto.getContent(), member);
        return "redirect:/board/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String boardModify(BoardDto boardDto,
                                 @PathVariable("id") Integer id, Principal principal) {

        Board board = this.boardService.getBoard(id);

        /*로그인한 사용자와 질문의 작성자가 동일하지않은 경우*/
        if(!board.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        /*수정할 질문의 제목과 내용을 보여줌*/
        boardDto.setSubject(board.getSubject());
        boardDto.setContent(board.getContent());
        return "boardForm";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String boardModify(@Valid BoardDto boardDto, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "boardForm";
        }
        Board board = this.boardService.getBoard(id);
        if (!board.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.boardService.modify(board, boardDto.getSubject(), boardDto.getContent());
        return String.format("redirect:/board/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String boardModify(Principal principal,
                                 @PathVariable("id") Integer id) {

        Board board = this.boardService.getBoard(id);

        /*로그인한 사용자와 질문의 작성자가 동일하지않은 경우*/
        if(!board.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        this.boardService.delete(board);
        return "redirect:/";
    }

}

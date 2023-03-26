package ToyProject.MBTIBoardWithComment.controller;

import ToyProject.MBTIBoardWithComment.domain.Board;
import ToyProject.MBTIBoardWithComment.domain.BoardPage;
import ToyProject.MBTIBoardWithComment.domain.Comment;
import ToyProject.MBTIBoardWithComment.domain.Member;
import ToyProject.MBTIBoardWithComment.dto.BoardDto;
import ToyProject.MBTIBoardWithComment.dto.CommentDto;
import ToyProject.MBTIBoardWithComment.repository.BoardPageRepository;
import ToyProject.MBTIBoardWithComment.repository.BoardRepository;
import ToyProject.MBTIBoardWithComment.service.BoardService;
import ToyProject.MBTIBoardWithComment.service.CommentService;
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
    private final BoardPageRepository boardPageRepository;
    private final CommentService commentService;

    @RequestMapping("/list")
    public String list(Model model,
                       @RequestParam(value="page", defaultValue="0") int page,
                       @RequestParam(value="kw",defaultValue = "")String kw){
        Page<Board> paging = this.boardService.getList(page, kw);
        model.addAttribute("paging", paging); /*페이징*/
        model.addAttribute("kw", kw); /*kw:검색어*/
        return "boardList";
    }

    /*질문상세*/ //댓글페이징추가
    @RequestMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, @Valid CommentDto commentDto,
                         @RequestParam(value="page", defaultValue="0") int page) {

        /*댓글페이징*/
        Board board = this.boardService.getBoard(id);

        Page<Comment> paging = this.commentService.getList(board, page);
        model.addAttribute("paging", paging);

        model.addAttribute("board", board);

        /*이전글다음글번호와 제목을 html에서 불러올수있게 model.addAttribute() 작성*/
        BoardPage boardPage = boardPageRepository.findByPages(id);
        model.addAttribute("prevID", boardPage.getPREVID());
        model.addAttribute("prevSub", boardPage.getPREV_SUB());
        model.addAttribute("nextID", boardPage.getNEXTID());
        model.addAttribute("nextSub", boardPage.getNEXT_SUB());

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
        this.boardService.create(boardDto.getSubject(), boardDto.getContent(), member, boardDto.getCategory());
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
        /*수정할 카테고리 보여줌*/
        boardDto.setCategory(board.getCategory());
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
        this.boardService.modify(board, boardDto.getSubject(), boardDto.getContent(), boardDto.getCategory());
        return String.format("redirect:/board/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String boardModify(Principal principal, @PathVariable("id") Integer id) {

        Board board = this.boardService.getBoard(id);

        /*로그인한 사용자와 질문의 작성자가 동일하지않은 경우*/
        if(!board.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        this.boardService.delete(board);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String boardVote(Principal principal, @PathVariable("id") Integer id){
        Board board = this.boardService.getBoard(id);
        Member member = this.memberService.getUser(principal.getName());
        this.boardService.vote(board, member);
        return String.format("redirect:/board/detail/%s", id);
    }

}

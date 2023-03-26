package ToyProject.MBTIBoardWithComment.controller;

import ToyProject.MBTIBoardWithComment.domain.Board;
import ToyProject.MBTIBoardWithComment.domain.Comment;
import ToyProject.MBTIBoardWithComment.domain.Member;
import ToyProject.MBTIBoardWithComment.dto.CommentDto;
import ToyProject.MBTIBoardWithComment.service.BoardService;
import ToyProject.MBTIBoardWithComment.service.CommentService;
import ToyProject.MBTIBoardWithComment.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/comment")
public class CommentController {
    private final BoardService boardService;
    private final CommentService commentService;
    private final MemberService memberService;

    /*답변생성*/
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id,
                               @Valid CommentDto commentDto, BindingResult bindingResult,
                               Principal principal) {

        Board board = this.boardService.getBoard(id);
        Member member = this.memberService.getUser(principal.getName());

        if(bindingResult.hasErrors()){
            model.addAttribute("board", board);
            return "boardDetail";
        }

        Comment comment = this.commentService.create(board,
                commentDto.getContent(), member);
        return String.format("redirect:/board/detail/%s#comment_%s",
                comment.getBoard().getId(), comment.getId());

        /*this.answerService.create(question, answerForm.getContent(), siteUser);
        return String.format("redirect:/question/detail/%s", id);*/
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String commentModify(CommentDto commentDto, @PathVariable("id") Integer id,
                               Principal principal){
        Comment comment = this.commentService.getComment(id);
        if(!comment.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        commentDto.setContent(comment.getContent());
        return "commentForm";
    }

    /*답변수정 POST*/
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answerModify(@Valid CommentDto commentDto, BindingResult bindingResult,
                               @PathVariable("id") Integer id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "commentForm";
        }
        Comment comment = this.commentService.getComment(id);
        if (!comment.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.commentService.modify(comment, commentDto.getContent());

        // return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
        return String.format("redirect:/board/detail/%s#comment_%s",
                comment.getBoard().getId(), comment.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String commentDelete(Principal principal, @PathVariable("id") Integer id) {
        Comment comment = this.commentService.getComment(id);
        if (!comment.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.commentService.delete(comment);
        return String.format("redirect:/board/detail/%s", comment.getBoard().getId());
    }

    /*답변추천*/
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String commentVote(Principal principal, @PathVariable("id")Integer id){
        Comment comment = this.commentService.getComment(id);
        Member member = this.memberService.getUser(principal.getName());
        this.commentService.vote(comment, member);

        // return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
        return String.format("redirect:/board/detail/%s#comment_%s",
                comment.getBoard().getId(), comment.getId());
    }
}

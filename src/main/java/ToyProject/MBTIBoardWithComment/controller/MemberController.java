package ToyProject.MBTIBoardWithComment.controller;

import ToyProject.MBTIBoardWithComment.domain.Member;
import ToyProject.MBTIBoardWithComment.dto.MemberDto;
import ToyProject.MBTIBoardWithComment.dto.MemberModifyDto;
import ToyProject.MBTIBoardWithComment.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequestMapping("/member")
@RequiredArgsConstructor
@Controller
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/signup")
    public String signup(MemberDto memberDto){
        return "member/memberForm";
    }

    @PostMapping("/signup")
    public String signup(@Valid MemberDto memberDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "member/memberForm";
        }

        if (!memberDto.getPassword1().equals(memberDto.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "member/memberForm";
        }

        /*중복회원가입막기*/
        try {
            memberService.create(memberDto.getUsername(),
                    memberDto.getEmail(), memberDto.getPassword1(), memberDto.getNickname());
        }catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "member/memberForm";
        }catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "member/memberForm";
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(){
        return "member/loginForm";
    }

    //로그인되어있는회원정보 뷰로던짐
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/info")
    public String memberinfo(MemberModifyDto memberModifyDto, Model model, Principal principal) {
        Member member = this.memberService.getUser(principal.getName());
        model.addAttribute("member", member);

        return "member/profile";
    }

    //로그인되어있는회원정보 수정
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/info")
    public String membermodify(MemberModifyDto memberModifyDto, Principal principal) {
        Member member = this.memberService.getUser(principal.getName());

        this.memberService.modify(member, memberModifyDto.getNickname(), memberModifyDto.getEmail());
        return "redirect:/member/info";
    }
}

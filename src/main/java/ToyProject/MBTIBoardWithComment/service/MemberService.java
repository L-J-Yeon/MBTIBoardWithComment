package ToyProject.MBTIBoardWithComment.service;

import ToyProject.MBTIBoardWithComment.domain.Member;
import ToyProject.MBTIBoardWithComment.exception.DataNotFoundException;
import ToyProject.MBTIBoardWithComment.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member create(String username, String email, String password, String nickname){
        Member member = new Member();
        member.setUsername(username);
        member.setNickname(nickname);
        member.setEmail(email);
        member.setPassword(passwordEncoder.encode(password));
        this.memberRepository.save(member);

        return member;
    }

    public Member getUser(String username){
        Optional<Member> member = this.memberRepository.findByusername(username);
        if(member.isPresent()){
            return member.get();
        }else{
            throw new DataNotFoundException("member not found");
        }
    }

    //회원수정
    public Member modify(Member member,String nickname, String email) {
        member.setNickname(nickname);
        member.setEmail(email);
        this.memberRepository.save(member);
        return member;
    }
}

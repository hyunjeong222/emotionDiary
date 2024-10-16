package com.springboot.emotionDiary.service;

import com.springboot.emotionDiary.config.jwt.JwtTokenProvider;
import com.springboot.emotionDiary.domain.Member;
import com.springboot.emotionDiary.dto.JwtToken;
import com.springboot.emotionDiary.dto.SignUpDto;
import com.springboot.emotionDiary.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public boolean existsByUserid(String userid) {
        return memberRepository.existsByUserid(userid);
    }

    @Transactional
    public Long signUp(SignUpDto request) {
        if (memberRepository.existsByUserid(request.getUserid())) {
            throw new IllegalArgumentException("이미 사용중인 아이디입니다.");
        }

        Member member = Member.builder()
                .userid(request.getUserid())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .nickname(request.getNickname())
                .personal(request.isPersonal())
                .roles(List.of("USER")) // 기본 권한 설정
                .build();

        return memberRepository.save(member).getId();
    }

    @Transactional
    public JwtToken signIn(String userid, String password) {
        // Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userid, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 검증된 인증 정보로 Jwt 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        return jwtToken;
    }
}

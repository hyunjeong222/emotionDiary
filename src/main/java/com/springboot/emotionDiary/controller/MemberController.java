package com.springboot.emotionDiary.controller;

import com.springboot.emotionDiary.config.security.SecurityUtil;
import com.springboot.emotionDiary.domain.Member;
import com.springboot.emotionDiary.dto.JwtToken;
import com.springboot.emotionDiary.dto.SignInDto;
import com.springboot.emotionDiary.dto.SignUpDto;
import com.springboot.emotionDiary.repository.MemberRepository;
import com.springboot.emotionDiary.service.MemberService;
import com.springboot.emotionDiary.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final TokenService tokenService;
    private final MemberRepository memberRepository;

    // 회원가입
    @PostMapping("/sign-up")
    public ResponseEntity<Map<String, Object>> signup(@Validated @RequestBody SignUpDto request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long userId = memberService.signUp(request);
            response.put("success", true);
            response.put("message", "회원가입 성공 !");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 아이디 중복 체크
    @GetMapping("/check-userid")
    public ResponseEntity<Boolean> checkUserId(@RequestParam(name = "userId") String userId) {
        boolean exists = memberService.existsByUserid(userId);
        return ResponseEntity.ok(exists);
    }

    // 로그인
    @PostMapping("/sign-in")
    public ResponseEntity<Map<String, Object>> signIn(@RequestBody SignInDto signInDto) {
        String userid = signInDto.getUserid();
        String password = signInDto.getPassword();

        JwtToken jwtToken = memberService.signIn(userid, password);

        Member member = memberRepository.findByUserid(userid)
                .orElseThrow(() -> new UsernameNotFoundException("해당 회원을 찾을 수 없습니다."));
        Long memberId = member.getId(); // 회원 ID

        Map<String, Object> response = new HashMap<>();
        response.put("grantType", jwtToken.getGrantType());
        response.put("accessToken", jwtToken.getAccessToken());
        response.put("refreshToken", jwtToken.getRefreshToken());
        response.put("refreshTokenExpirationTime", jwtToken.getRefreshTokenExpirationTime());
        response.put("memberId", memberId); // memberId 추가

        return ResponseEntity.ok(response);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        String userId = SecurityUtil.getCurrentUserid(); // 현재 사용자 ID를 가져오는 메서드

        tokenService.deleteRefreshToken(userId);
        return ResponseEntity.ok().build();
    }
}

package com.springboot.emotionDiary.config.jwt;

import com.springboot.emotionDiary.config.dao.RedisDao;
import com.springboot.emotionDiary.dto.JwtToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L;              // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;    // 7일
    private final Key key;

    // redis
    private final RedisDao redisDao;

    // application.yml secret 값 가져와서 key에 저장
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
                            RedisDao redisDao) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        // redis
        this.redisDao = redisDao;
    }

    // Member 정보(인증 객체, Authentication)를 가지고 AccessToken, RefreshToken 생성하는 메서드
    // AccessToken : 인증된 사용자의 권한 정보와 만료 시간을 담고있음
    // RefreshToken : AccessToken 갱신을 위해 사용
    public JwtToken generateToken(Authentication authentication) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName()) // 사용자 id
                .claim("userid", authentication.getName()) // 추가적으로 userid 클레임 추가 *
                .claim(AUTHORITIES_KEY, authorities) // 권한
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // redis
        redisDao.setValues(authentication.getName(), refreshToken, Duration.ofMillis(REFRESH_TOKEN_EXPIRE_TIME));

        return JwtToken.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME)
                .build();
    }

    // Jwt 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    // 주어진 Access token을 복호화하여 사용자 인증 정보(Authentication) 생성
    // 토큰의 Claims에서 권한 정보를 추출, User 객체를 생성하여 Authentication 객체로 반환
    public Authentication getAuthentication(String accessToken) {
        // Jwt 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        // Collection<? extends GrantedAuthority>로 리턴받는 이유
        // 권한 정보를 다양한 타입의 객체로 처리할 수 있고, 더 큰 유연성과 확장성을 가질 수 있음
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // UserDetails : interface, User : UserDetails 구현한 class
        // UserDetails 객체 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // 토큰 정보 검증 메서드 - 주어진 토큰을 검증하여 유효성 확인
    // Jwts.parserBuilder를 사용하여 토큰의 서명 키를 설정하고, 예외 처리를 통해 토큰의 유효성 여부를 판단
    // claim.getSubject()는 주어진 토큰의 클레임에서 "sub" 클레임의 값을 반환 -> 토큰의 주체 (사용자의 식별자나 이메일 주소)
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.", e);
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.", e);
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.", e);
        } catch (IllegalArgumentException e) { // 토큰이 올바른 형식이 아니거나 클레임이 비어있는 경우 등에 발생
            log.info("JWT 토큰이 잘못되었습니다.", e);
        }
        return false;
    }

    // accessToken
    // Claims : 토큰에서 사용할 정보의 조각
    // 주어진 Access token 복호화하고, 만료된 토큰인 경우에도 Claims 반환
    // parseClaimsJws() 메서드가 Jwt 토큰의 검증과 파싱을 모두 수행
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Long getExpiration(String accessToken) {
        // accessToken 남은 유효시간
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().getExpiration();
        // 현재 시간
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }
}

package com.springboot.emotionDiary.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Member implements UserDetails { // UserDetails를 상속받아 인증 객체로 사용
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private Long id;

    @Column(name = "userid", nullable = false, unique = true)
    private String userid;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "personal", nullable = false)
    private boolean personal;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    // 멤버가 가지고 있는 권한(authority) 목록을 SimpleGrantedAuthority 변환하여 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return userid;
    }

    @Override
    public String getPassword() {
        return password;
    }

    // 나머지 Override 메서드들 전부 true 반환하도록 설정
    // 계정 만료 여부 반환
    @Override
    public boolean isAccountNonExpired() {
        return true; // true : 만료되지 않음
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 패스워드 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // true : 만료되지 않음
    }

    // 계정 사용 가능 여부 반환
    @Override
    public boolean isEnabled() {
        return true; // true : 사용 가능
    }
}

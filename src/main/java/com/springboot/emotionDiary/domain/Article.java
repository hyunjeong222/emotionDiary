package com.springboot.emotionDiary.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Table(name = "article", indexes = {@Index(name = "idx_year_month", columnList = "created_year_month")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private Long id;

    @Column(name = "created_at", nullable = false, updatable = true)
    private LocalDate createdAt;

    // yearMonth 추가
    @Column(name = "created_year_month", nullable = false, updatable = true)
    private String yearMonth;

    @Column(name = "emotion_id", nullable = false, updatable = true)
    private int emotionId;

    @Lob
    @Column(name = "content", nullable = false, updatable = true, columnDefinition = "LONGTEXT") // content - not null
    private String content;

    // Member 테이블과의 다대일 관계 설정
    @JsonIgnore // 직렬화 제외
    @ManyToOne(fetch = FetchType.LAZY) // 여러개의 Article이 하나의 Member를 가리킴
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public void update(LocalDate createdAt, String content, int emotionId) {
        this.createdAt = createdAt;
        this.content = content;
        this.emotionId = emotionId;
        this.yearMonth = this.createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }
}

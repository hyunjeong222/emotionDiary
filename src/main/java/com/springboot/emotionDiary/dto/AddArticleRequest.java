package com.springboot.emotionDiary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddArticleRequest {
    private Long memberId;
    private LocalDate createdAt;
    private String yearMonth;
    private int emotionId;
    private String content;

    // createdAt을 설정할 때 yearMonth도 자동으로 계산
    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        this.yearMonth = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }
}

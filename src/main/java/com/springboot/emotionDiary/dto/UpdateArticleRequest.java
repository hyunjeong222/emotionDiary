package com.springboot.emotionDiary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateArticleRequest {
    private String content;
    private LocalDate createdAt;
    private int emotionId;
}

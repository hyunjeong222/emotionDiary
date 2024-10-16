package com.springboot.emotionDiary.dto;

import com.springboot.emotionDiary.domain.Article;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class ArticleResponse {
    private Long id;
    private LocalDate createdAt;
    private int emotionId;
    private String content;
    private Long memberId;
    private String yearMonth;
    private String year;
    private String month;


    public ArticleResponse(Article article) {
        this.id = article.getId();
        this.createdAt = article.getCreatedAt();
        this.yearMonth = article.getYearMonth();
        this.emotionId = article.getEmotionId();
        this.content = article.getContent();
        this.memberId = article.getMember().getId();

        this.year = this.yearMonth.substring(0, 4);
        this.month = this.yearMonth.substring(5, 7);
    }
}

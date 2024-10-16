package com.springboot.emotionDiary.repository;

import com.springboot.emotionDiary.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByMemberIdAndYearMonth(Long memberId, String yearMonth);
}
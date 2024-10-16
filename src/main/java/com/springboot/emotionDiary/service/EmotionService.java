package com.springboot.emotionDiary.service;

import com.springboot.emotionDiary.domain.Article;
import com.springboot.emotionDiary.domain.Member;
import com.springboot.emotionDiary.dto.AddArticleRequest;
import com.springboot.emotionDiary.dto.ArticleResponse;
import com.springboot.emotionDiary.dto.UpdateArticleRequest;
import com.springboot.emotionDiary.repository.ArticleRepository;
import com.springboot.emotionDiary.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EmotionService {
    @Autowired
    private final ArticleRepository articleRepository;

    @Autowired
    private final MemberRepository memberRepository;

    // 블로그 글 추가 메소드
    public Article save(AddArticleRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        Article article = Article.builder()
                .createdAt(request.getCreatedAt())
                .emotionId(request.getEmotionId())
                .content(request.getContent())
                .member(member)
                .yearMonth(request.getYearMonth())
                .build();

        return articleRepository.save(article);
    }

    // 게시글 조회
    public List<ArticleResponse> getArticlesByMemberIdAndDate(Long memberId, String yearMonth) {
        // memberId 해당 회원이 작성한 글 목록을 월 별로 가져옴
        List<Article> articles = articleRepository.findByMemberIdAndYearMonth(memberId, yearMonth);
        // 최신순으로 정렬
        articles.sort(Comparator.comparing(Article::getCreatedAt).reversed());

        return articles.stream().map(ArticleResponse::new).collect(Collectors.toList());
    }

    // 글 하나 조회
    public Article findById(long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
    }

    // 글 수정
    @Transactional
    public Article update(long id, UpdateArticleRequest request) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

        article.update(request.getCreatedAt(), request.getContent(), request.getEmotionId());
        return article;
    }

    // 글 삭제
    public void delete(long id) {
        articleRepository.deleteById(id);
    }
}

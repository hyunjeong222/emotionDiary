package com.springboot.emotionDiary.controller;

import com.springboot.emotionDiary.domain.Article;
import com.springboot.emotionDiary.dto.AddArticleRequest;
import com.springboot.emotionDiary.dto.ArticleResponse;
import com.springboot.emotionDiary.dto.UpdateArticleRequest;
import com.springboot.emotionDiary.service.EmotionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/content")
public class ContentController {
    @Autowired
    private EmotionService emotionService;

    @PostMapping("/role/newArticle")
    public ResponseEntity<Map<String, String>> newArticleRole() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "redirect");
        response.put("redirectUrl", "/content/newArticle"); // 이동할 URL
        return ResponseEntity.ok(response);
    }

    @GetMapping("/newArticle")
    public ModelAndView newArticle (@RequestParam(name = "id", required = false) Long id) {
        ModelAndView mav = new ModelAndView("/content/newArticle");
        if (id == null) { // 아이디가 존재하지 않는다면 생성
            mav.addObject("articles", new ArticleResponse());
        } else { // 아이디가 존재한다면 수정
            Article article = emotionService.findById(id);
            mav.addObject("articles", new ArticleResponse(article));
        }
        return mav;
    }

    // 글 추가
    @PostMapping("/addArticle")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request) {
        Article article = emotionService.save(request);
        return ResponseEntity.ok(article);
    }

    // 글 조회
    @GetMapping("/memberArticles/{memberId}")
    public Object getMemberArticles(@PathVariable("memberId") Long memberId,
                                          @RequestParam(value = "yearMonth", required = false) String yearMonth) {
        // yearMonth가 null이면 현재 달로 설정
        if (yearMonth == null) {
            yearMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            List<ArticleResponse> articles = emotionService.getArticlesByMemberIdAndDate(memberId, yearMonth);
            int articleCount = articles.size();

            ModelAndView mav = new ModelAndView("index");
            mav.addObject("articles", articles);
            mav.addObject("articleCount", articleCount);
            mav.addObject("selectedYearMonth", yearMonth); // 선택된 연/월 추가 담아서 리턴

            return mav; // 뷰를 반환
        } else {
            // 해당 회원의 특정 연/월에 해당하는 게시글 가져오기
            List<ArticleResponse> articles = emotionService.getArticlesByMemberIdAndDate(memberId, yearMonth);
            int articleCount = articles.size(); // 글의 개수 계산

            Map<String, Object> response = new HashMap<>();
            response.put("articles", articles);
            response.put("articleCount", articleCount);
            response.put("selectedYearMonth", yearMonth);

            return ResponseEntity.ok(response);
        }
    }

    // 글 개별 조회
    @GetMapping("/articles/{id}")
    public ModelAndView getArticle(@PathVariable("id") Long id) {
        Article article = emotionService.findById(id);
        ModelAndView mav = new ModelAndView("/content/viewArticle");
        mav.addObject("articles", new ArticleResponse(article));

        return mav;
    }

    // 게시글 수정
    @PutMapping("/newArticle/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable("id") long id, @RequestBody UpdateArticleRequest request){
        Article updateArticle = emotionService.update(id, request);

        return ResponseEntity.ok()
                .body(updateArticle);
    }

    // 게시글 삭제
    @DeleteMapping("/deleteArticle/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable("id") long id) {
        emotionService.delete(id);

        return ResponseEntity.ok().build();
    }
}

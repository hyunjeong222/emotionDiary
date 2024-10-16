package com.springboot.emotionDiary.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    @GetMapping("/")
    public String index () {
        return "/index";
    }

    @GetMapping("/join")
    public String join() {
        return "/member/join";
    }

    @GetMapping("/login")
    public String login() {
        return "/member/login";
    }
}

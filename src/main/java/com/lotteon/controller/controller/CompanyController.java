package com.lotteon.controller.controller;

import com.lotteon.dto.requestDto.PostRecruitDto;
import com.lotteon.service.article.RecruitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/*
* 이름 : 이상훈
* 날짜 : 2024-10-25
* 작업내용 : 채용공고 목록 불러오기
* */
@Controller
@RequestMapping("/company")
@RequiredArgsConstructor
@Log4j2
public class CompanyController {
    
    private final RecruitService recruitService;

    @GetMapping("/index")
    public String index(Model model) {

        return "pages/company/index";
    }

    @GetMapping("/culture")
    public String culture(Model model) {
        return "pages/company/culture";
    }

    @GetMapping("/recruit")
    public String recruit(Model model) {
        Page<PostRecruitDto> recruits = recruitService.findAll(0);
        model.addAttribute("recruits", recruits);
        return "pages/company/recruit";
    }

    @GetMapping("/story")
    public String story(Model model) {
        return "pages/company/story";
    }

    @GetMapping("/media")
    public String media(Model model) {
        return "pages/company/media";
    }










}


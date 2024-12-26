package com.lotteon.controller.controller;

import com.lotteon.config.MyUserDetails;
import com.lotteon.dto.requestDto.NoticeRequestDto;
import com.lotteon.dto.responseDto.GetArticleCategoryDto;
import com.lotteon.dto.responseDto.NoticeResponseDto;
import com.lotteon.entity.category.CategoryArticle;
import com.lotteon.service.article.NoticeService;
import com.lotteon.service.category.CategoryArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Controller
@RequestMapping("/admin/cs")
@RequiredArgsConstructor
public class AdminCsNoticeController {

    private final NoticeService noticeService;
    private final CategoryArticleService categoryArticleService;

    // 공지사항 목록 조회 (페이지네이션 + 카테고리 및 제목 필터링 추가)
    @GetMapping("/notices")
    public String notices(Model model,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(required = false) String type,  // 카테고리 필터링 파라미터
                          @RequestParam(required = false) String title) { // 제목 검색 파라미터

        // 필터링과 페이지네이션 적용
        Pageable pageable = PageRequest.of(page, 10);

        // 1차 카테고리 목록 조회
        List<GetArticleCategoryDto> cate1 = categoryArticleService.findCategory(1, 1); // category_level = 1인 카테고리만 조회
        model.addAttribute("cate1", cate1); // 1차 카테고리 목록을 모델에 추가


        // 공지사항 목록 조회 (카테고리 및 제목 필터링 적용)
        Page<NoticeResponseDto> noticePage;
        if (type != null && !type.equals("typeselect") && title != null && !title.isEmpty()) {
            // 카테고리와 제목이 모두 지정된 경우
            noticePage = noticeService.getNoticesByCate1AndTitle(type, title, pageable);
        } else if (type != null && !type.equals("typeselect")) {
            // 카테고리만 지정된 경우
            noticePage = noticeService.getNoticesByCate1(type, pageable);
        } else if (title != null && !title.isEmpty()) {
            // 제목만 지정된 경우
            noticePage = noticeService.getNoticesByTitle(title, pageable);
        } else {
            // 전체 조회
            noticePage = noticeService.getNotices(null, pageable);
        }

        model.addAttribute("notices", noticePage.getContent());
        model.addAttribute("page", page);
        model.addAttribute("totalPages", noticePage.getTotalPages());
        model.addAttribute("selectedType", type); // 선택된 카테고리 유지
        model.addAttribute("title", title); // 검색된 제목 유지

        return "pages/admin/cs/notice/list"; // 공지사항 목록 뷰로 이동
    }

    // 공지사항 상세 조회 (조회수 증가 포함)
    @GetMapping("/notice/{id}")
    public String notice(@PathVariable Long id, Model model) {
        NoticeResponseDto notice = noticeService.incrementViewsAndGetNotice(id);
        model.addAttribute("notice", notice); // 공지사항 데이터를 모델에 추가
        return "pages/admin/cs/notice/view"; // 상세 페이지 뷰 반환
    }

    // 공지사항 작성 폼
    @GetMapping("/notice/write")
    public String noticeWriteForm(Model model, Authentication auth) {
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        model.addAttribute("user", user);

        // 1차 카테고리 가져오기
        List<GetArticleCategoryDto> cate1 = categoryArticleService.findCategory(1, 1);
        model.addAttribute("cate1", cate1);

        // 2차 카테고리 가져오기
        List<GetArticleCategoryDto> cate2 = categoryArticleService.findCategory(1, 2);
        model.addAttribute("cate2", cate2);

        return "pages/admin/cs/notice/write";
    }

    // 2차 카테고리 동적 로드 API
    @GetMapping("/notice/categories/child")
    @ResponseBody
    public List<GetArticleCategoryDto> getChildCategories(@RequestParam Long parentId) {
        List<GetArticleCategoryDto> childCategories = categoryArticleService.getChildCategories(parentId)
                .stream()
                .map(CategoryArticle::toGetArticleCategoryDto)
                .collect(Collectors.toList());

        log.info("2차 카테고리 조회 결과: " + childCategories); // 로그로 데이터 확인
        return childCategories;
    }

    // 공지사항 작성 처리
    @PostMapping("/notice/write")
    public String noticeWrite(@ModelAttribute NoticeRequestDto dto) {
        log.info("공지사항 작성 요청: " + dto);
        noticeService.saveNotice(dto);
        return "redirect:/admin/cs/notices";
    }

    // 공지사항 수정 폼
    @GetMapping("/notice/modify/{id}")
    public String noticeModifyForm(@PathVariable Long id, Model model) {
        NoticeResponseDto notice = noticeService.getNotice(id);
        model.addAttribute("notice", notice);
        return "pages/admin/cs/notice/modify"; // 수정 폼으로 이동
    }

    // 공지사항 수정 처리
    @PostMapping("/notice/modify/{id}")
    public String noticeModify(@PathVariable Long id, @ModelAttribute NoticeRequestDto noticeRequestDto) {
        noticeService.updateNotice(id, noticeRequestDto); // 수정 처리
        return "redirect:/admin/cs/notice/" + id;
    }

    // 공지사항 삭제 처리 (POST 요청)
    @PostMapping("/notice/delete/{id}")
    public String deleteNotice(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            noticeService.deleteNotice(id);
            redirectAttributes.addFlashAttribute("message", "삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "삭제 실패했습니다.");
        }
        return "redirect:/admin/cs/notices";
    }

    // 선택된 공지사항 삭제
    @PostMapping("/notice/deleteSelected")
    @ResponseBody
    public Map<String, Object> deleteSelectedNotices(@RequestBody List<Long> ids) {
        Map<String, Object> result = new HashMap<>();
        try {
            noticeService.deleteSelectedNotices(ids);
            result.put("success", true);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    @GetMapping("/notice/view/{id}")
    public String viewNotice(@PathVariable Long id, Model model) {
        NoticeResponseDto notice = noticeService.getNotice(id);  // ID를 통해 공지사항 조회
        model.addAttribute("notice", notice);  // 조회한 공지사항을 모델에 추가
        return "pages/admin/cs/notice/view";  // 보기 페이지로 이동
    }
}

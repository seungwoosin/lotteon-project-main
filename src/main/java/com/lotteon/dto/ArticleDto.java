package com.lotteon.dto;

import com.lotteon.entity.article.Faq;
import com.lotteon.entity.article.Qna;
import com.lotteon.entity.category.CategoryArticle;
import com.lotteon.entity.member.Member;
import lombok.*;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/*
 *  이름 : 박경림
 *  날짜 : 2024-10-29
 *  작업내용 :  fromEntity 메서드를 오버로딩하여 추가
 *
 * */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ArticleDto {
    private Long id;
    private String title;        // 공통: 제목 (FAQ, Notice, QnA 모두에서 사용)
    private String content;      // 공통: 내용 (FAQ, Notice, QnA 모두에서 사용)
    private String answer;       // QnA 전용: 답변 내용
    private LocalDateTime rdate;
    private int views;
    private int state;           // QnA 전용: 답변 상태 (답변 완료/대기 등)
    private int type;            // QnA 전용: 문의 대상 (판매자 or 관리자)
    private Long cate1Id;
    private Long cate2Id;
    private String cate1Name;
    private String cate2Name;
    private Long memId;
    private CategoryArticle cate1;
    private CategoryArticle cate2;
    private String adminName;    // 관리자 이름 (관리자용 추가 필드)
    private String adminEmail;   // 관리자 이메일 (관리자용 추가 필드)
    private Member member;
    private LocalDateTime updateDate;  // 업데이트된 날짜 (필요 시 추가)
    private String status;       // 글 상태 (예: '작성 중', '게시 완료' 등)
    private Long sellId;         // QnA 전용: 판매자
    private String sellCompany;

    // 2차 카테고리별 FAQ 목록을 포함할 리스트
    private List<CategoryFaq> categoryFaqs;

    // 2차 카테고리와 FAQ 목록을 저장할 내부 클래스 정의
    @Getter
    @Setter
    @AllArgsConstructor
    public static class CategoryFaq {
        private String categoryName; // 2차 카테고리 이름
        private List<ArticleDto> faqs; // FAQ 목록
    }

    // 오버로딩: 같은 이름(fromEntity)을 사용 인자의 엔티티 타입(Faq, Qna)에 따라 서로 다른 처리
    public static ArticleDto fromEntity(Faq faq) {
        return ArticleDto.builder()
                .id(faq.getId())
                .title(faq.getFaqTitle()) // title 필드에 faqTitle 매핑
                .content(faq.getFaqContent())
                .rdate(faq.getFaqRdate())
                .views(faq.getFaqViews())
                .cate1(faq.getCate1())
                .cate2(faq.getCate2())
                .cate1Id(faq.getCate1() != null ? faq.getCate1().getCategoryId() : null)
                .cate2Id(faq.getCate2() != null ? faq.getCate2().getCategoryId() : null)
                .member(faq.getMember())
                .memId(faq.getMember() != null ? faq.getMember().getId() : null)
                .build();
    }


    public static ArticleDto fromEntity(Qna qna) {
        if(qna.getSeller()==null){
            return ArticleDto.builder()
                    .id(qna.getId())
                    .title(qna.getQnaTitle()) // **수정된 부분: title 필드에 qnaTitle 매핑**
                    .content(qna.getQnaContent())
                    .rdate(qna.getQnaRdate())
                    .views(qna.getQnaViews())
                    .cate1(qna.getCate1())
                    .cate2(qna.getCate2())
                    .cate1Id(qna.getCate1() != null ? qna.getCate1().getCategoryId() : null)
                    .cate2Id(qna.getCate2() != null ? qna.getCate2().getCategoryId() : null)
                    .member(qna.getMember())
                    .memId(qna.getMember() != null ? qna.getMember().getId() : null)
                    .answer(qna.getQnaAnswer()) // **수정된 부분: QnA 전용 필드 answer 매핑**
                    .state(qna.getQnaState())   // **수정된 부분: QnA 전용 필드 state 매핑**
                    .type(qna.getQnaType())     // **수정된 부분: QnA 전용 필드 type 매핑**
                    .build();
        } else {
            return ArticleDto.builder()
                    .id(qna.getId())
                    .title(qna.getQnaTitle()) // **수정된 부분: title 필드에 qnaTitle 매핑**
                    .content(qna.getQnaContent())
                    .rdate(qna.getQnaRdate())
                    .views(qna.getQnaViews())
                    .cate1(qna.getCate1())
                    .cate2(qna.getCate2())
                    .cate1Id(qna.getCate1() != null ? qna.getCate1().getCategoryId() : null)
                    .cate2Id(qna.getCate2() != null ? qna.getCate2().getCategoryId() : null)
                    .member(qna.getMember())
                    .memId(qna.getMember() != null ? qna.getMember().getId() : null)
                    .answer(qna.getQnaAnswer()) // **수정된 부분: QnA 전용 필드 answer 매핑**
                    .state(qna.getQnaState())   // **수정된 부분: QnA 전용 필드 state 매핑**
                    .type(qna.getQnaType())     // **수정된 부분: QnA 전용 필드 type 매핑**
                    .sellId(qna.getSeller().getId())    // **추가된 부분: 셀러 ID 매핑**
                    .build();
        }

    }
}
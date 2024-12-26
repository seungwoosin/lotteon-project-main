package com.lotteon.dto.responseDto;

import lombok.*;

/*
     날짜 : 2024/11/06 (수)
     이름 : 김민희
     내용 : 나의 쇼핑정보 > 나의설정 DTO 생성

*/

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class GetMyInfoDTO {

    // Customer -> mem_id
    // Member -> id

    // 나의 쇼핑정보 > 나의설정
    // 사용자 번호(페이지 조회 시 필요), 아이디, 이름, 생년월일, 이메일(/@/), 휴대폰(3/4/4), 주소(우편/기본/상세)
    private Long    custId;         // 사용자 번호

    private String  memUid;         // 아이디
    private String  custName;       // 이름
    private String  custBirth;      // 생일

    private String  custEmail1;      // 이메일 @ 앞부분
    private String  custEmail2;      // 이메일 @ 뒷부분

    private String  custHp1;         // 휴대폰 번호 010
    private String  custHp2;         // 휴대폰 번호 두번째 -1234
    private String  custHp3;         // 휴대폰 번호 세번째 -5678

    private String custAddr1;        // 우편
    private String custAddr2;        // 주소
    private String custAddr3;        // 상세주소

}

package com.lotteon.dto.requestDto;

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
public class PatchMyInfoDTO {

    // Customer -> mem_id
    // Member -> id

    // 나의 쇼핑정보 > 나의설정
    // 사용자 번호(페이지 조회 시 필요), 아이디, 이름, 생년월일, 이메일(/@/), 휴대폰(3/4/4), 주소(우편/기본/상세)
    private Long    custId;         // 사용자 번호
    private String  memPwd;         // 사용자 비밀번호

    private String  custEmail;

    private String  custHp;    // 휴대폰 번호 세번째 -5678

    private String custAddr;   // 상세주소

}

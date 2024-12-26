package com.lotteon.controller.apicontroller;

import com.lotteon.dto.responseDto.GetAdminUserDTO;
import com.lotteon.entity.member.Member;
import com.lotteon.repository.member.MemberRepository;
import com.lotteon.service.AuthService;
import com.lotteon.service.member.MemberService;
import com.lotteon.service.point.PointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eclipse.angus.mail.iap.Response;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/*
     날짜 : 2024/10/25 (금)
     이름 : 김민희
     내용 : 관리자 회원목록 수정 API Controller 생성
     
     수정이력
      - 2024/10/27 (일) 김민희 - 회원수정 팝업호출 기능 메서드 추가
      - 2024/11/06 (수) 김주경 - 선택수정 기능 메서드 추가

*/

@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
@Log4j2
public class ApiAdminUserController {

    private final AuthService authService;
    private final MemberRepository memberRepository;
    private final PointService pointService;


    // 1. 관리자 회원수정 정보조회 (+팝업호출)
    @GetMapping("/user/{id}")
    public ResponseEntity<?> popCust(@PathVariable("id") Long id) {
        log.info("id: " + id + "에 해당하는 회원 정보");

        // `AuthService.popCust(id)`에서 역할에 따른 로직 분기
        Optional<Member> memberOpt = memberRepository.findByCustomer_id(id);
        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            String role = member.getMemRole();

            // `customer` 역할일 경우 Customer 정보 DTO 반환
            if (role.equals("customer")) {
                GetAdminUserDTO custPop = authService.popCust(id);  // 기존 방식 유지
                System.out.println(custPop.getMemRole());
                log.info("customer 정보: " + custPop);
                return ResponseEntity.ok().body(custPop);

            // `guest` 역할일 경우 새로운 DTO 반환
            } else {
                GetAdminUserDTO guestPop = authService.popGuest(id);  // 새로운 guest용 메서드
                System.out.println(guestPop.getMemRole());
                log.info("guest 정보: " + guestPop);
                return ResponseEntity.ok().body(guestPop);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 ID를 가진 회원 정보가 없습니다.");
        }
    }

    // 2. 관리자 회원 수정
    @PutMapping("/user/{id}")
    public ResponseEntity<GetAdminUserDTO> updateCust(
                                @PathVariable("id") Long id,
                                @RequestBody GetAdminUserDTO getAdminUserDTO) {

        log.info("minhee : "+id);
        log.info("getAdminUserDTO : "+ getAdminUserDTO);

        GetAdminUserDTO updatedCust = authService.updateCust(id,getAdminUserDTO);

        return ResponseEntity.ok().body(updatedCust);
    }


    // 2. 관리자 회원목록 선택수정
    @PatchMapping("/select")
    public ResponseEntity<?> modifyCustGrade(@RequestBody Map<String, List<?>> arrays) {
        List<Long> custIds = ((List<?>) arrays.get("ids"))
                .stream()
                .map(id -> Long.valueOf(id.toString()))
                .collect(Collectors.toList());
        List<String> custGrades = (List<String>) arrays.get("grades");
        Boolean success = authService.modifyCustsGradeById(custIds,custGrades); // 담을 물건 준비
        log.info(success);

        Map<String, Object> response = new HashMap<>();
        response.put("success", success);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/{state}")
    public ResponseEntity<?> modifyCustState(@PathVariable("id") Long id, @PathVariable("state") String state) {
        Boolean success = authService.modifyCustStateById(id,state);
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/point")
    public ResponseEntity<?> deletePoint(
            @RequestParam List<Long> id
    ){
        pointService.deleteSelectedPoint(id);
        return ResponseEntity.ok().build();
    }
}

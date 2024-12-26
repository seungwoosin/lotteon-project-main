package com.lotteon.controller.apicontroller;

import com.lotteon.dto.requestDto.PostRecruitDto;
import com.lotteon.dto.responseDto.GetRecruitDto;
import com.lotteon.service.article.RecruitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/cs")
public class ApiAdminRecruitController {

    private final RecruitService recruitService;
    @PostMapping("/recruit")
    public ResponseEntity<?> postRecruit(@RequestBody PostRecruitDto dto){
        recruitService.insertRecruit(dto);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/recruit")
    public ResponseEntity<?> getRecruit(@RequestParam Long id){
        GetRecruitDto recruit = recruitService.findById(id);
        Map<String,Object> map = new HashMap<>();
        map.put("recruit",recruit);
        return ResponseEntity.ok().body(map);
    }

    @PutMapping("/recruit")
    public ResponseEntity<?> updateRecruit(@RequestBody PostRecruitDto dto){

        recruitService.updateRecruit(dto);
        return ResponseEntity.ok().build();
    }
}

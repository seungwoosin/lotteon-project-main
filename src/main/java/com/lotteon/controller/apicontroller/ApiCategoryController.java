package com.lotteon.controller.apicontroller;

import com.lotteon.dto.responseDto.GetCategoryDto;
import com.lotteon.service.category.CategoryProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
 * 이름 : 이상훈
 * 날짜 : 2024-10-26
 * 작업내용 : 카테고리 출력 레디스 인메모리디비 사용하기
 * */
@RestController
@RequiredArgsConstructor
public class ApiCategoryController {

    private final CategoryProductService categoryProductService;

    @GetMapping("/categories")
    public ResponseEntity<?> getCate(
            @RequestParam Long id
    ){
        List<GetCategoryDto> cates =categoryProductService.findCategory2(id);

        Map<String,Object> cates2 = categoryProductService.findCategory3(id);

        Map<String, Object> map = new HashMap<>();
        map.put("cates", cates);
        map.put("cates2", cates2);
        return ResponseEntity.ok(map);
    }

    @GetMapping("/admin/prod/cate1")
    public ResponseEntity<?> getCate1(
            @RequestParam Long id
    ){
        List<GetCategoryDto> cates = categoryProductService.findCategory2(id);
        Map<String, Object> map = new HashMap<>();

        map.put("cates", cates);
        return ResponseEntity.ok(map);
    }

    @DeleteMapping("/admin/prod/category")
    public ResponseEntity<String> deleteCategory(
            @RequestParam Long id
    ){
        String result = categoryProductService.deleteCategory(id);
        if(result.equals("SU")){
            return ResponseEntity.ok().body(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PostMapping("/admin/prod/category")
    public ResponseEntity<String> addCategory(
            @RequestBody GetCategoryDto getCategoryDto
    ){
        System.out.println(getCategoryDto);
        categoryProductService.postCategory(getCategoryDto);

        return ResponseEntity.ok().body("");
    }

    @PutMapping("/admin/prod/category")
    public ResponseEntity<String> modifyCategory(
            @RequestBody GetCategoryDto getCategoryDto
    ){
        categoryProductService.putCategory(getCategoryDto);
        return ResponseEntity.ok().body("");
    }
}

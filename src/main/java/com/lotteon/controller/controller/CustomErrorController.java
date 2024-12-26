package com.lotteon.controller.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        // 에러 상태 코드 가져오기
        Object status = request.getAttribute("javax.servlet.error.status_code");

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == 404) {
                return "pages/error-404"; // 404 에러 페이지
            }
            else if(statusCode == 500) {
                return "pages/error-500"; // 500 에러 페이지
            }
            // 다른 상태 코드에 대한 처리를 추가할 수 있습니다.
        }

        return "pages/error"; // 일반 에러 페이지
    }
}

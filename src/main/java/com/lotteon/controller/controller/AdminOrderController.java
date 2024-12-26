package com.lotteon.controller.controller;

import com.lotteon.config.MyUserDetails;
import com.lotteon.dto.requestDto.GetDeliveryDto;
import com.lotteon.dto.responseDto.cartOrder.ResponseAdminOrderDto;
import com.lotteon.dto.responseDto.cartOrder.ResponseOrdersDto;
import com.lotteon.entity.member.Customer;
import com.lotteon.service.product.OrderItemService;
import com.lotteon.service.product.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/order")
@RequiredArgsConstructor
@Log4j2
public class AdminOrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @ModelAttribute
    public void pageIndex(Model model) {
        model.addAttribute("config",getSideValue());
    }
    private String getSideValue() {
        return "order";  // 실제 config 값을 여기에 설정합니다.
    }

    @GetMapping("/orders")
    public String orders(Model model,Authentication authentication,
                         @RequestParam(name = "page",defaultValue = "0") int page,
                         @RequestParam(value = "searchType",defaultValue = "0") String searchType,
                         @RequestParam(value = "keyword",defaultValue = "0") String keyword
    ) {
        MyUserDetails auth =(MyUserDetails) authentication.getPrincipal();
        String role = auth.getUser().getMemRole();
        log.info("auth 롤 체크 "+role);

        Long sellerId = auth.getUser().getSeller().getId();
        Page<ResponseAdminOrderDto> orders;

        if (role.equals("admin") && searchType.equals("0")) {
            // Admin 전체 조회
            orders = orderService.selectedAdminOrdersByAdmin(page);
        } else if (role.equals("admin")) {
            // Admin 키워드 검색 조회
            orders = orderService.findAdminOrdersByKeyword(page, searchType, keyword);
        } else if (searchType.equals("0")) {
            // Seller 전체 조회
            orders = orderService.selectedAdminOrdersBySeller(page);
        } else {
            // Seller 키워드 검색 조회
            orders = orderService.findSellerOrdersByKeyword(page, searchType, keyword);
        }

        log.info("컨트롤러 페이지 오더 데이터 확인 "+orders.getContent());

        model.addAttribute("orders", orders);
        model.addAttribute("page", page);
        model.addAttribute("totalPages", orders.getTotalPages());
        model.addAttribute("active","orders");
        return "pages/admin/order/list";
    }

    @GetMapping("/deliverys")
    public String deliverys(
            Model model,
            @RequestParam(value = "page",defaultValue = "0") int page,
            @RequestParam(value = "searchType",defaultValue = "0") String searchType,
            @RequestParam(value = "keyword",defaultValue = "0") String keyword
    ) {
        model.addAttribute("active","deliverys");
        Page<GetDeliveryDto> deliverys;
        if(searchType.equals("0")){
            deliverys = orderItemService.findAllBySeller(page);
        } else {
            deliverys = orderItemService.findAllBySellerAndSearchType(page,searchType,keyword);
        }
        model.addAttribute("deliverys", deliverys);
        model.addAttribute("page",page);
        model.addAttribute("totalPages", deliverys.getTotalPages());
        model.addAttribute("searchType",searchType);
        model.addAttribute("keyword",keyword);
        return "pages/admin/order/delivery";
    }
}

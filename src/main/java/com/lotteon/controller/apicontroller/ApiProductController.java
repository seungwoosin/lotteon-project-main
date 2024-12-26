package com.lotteon.controller.apicontroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lotteon.config.MyUserDetails;
import com.lotteon.dto.requestDto.GetProductNamesDto;
import com.lotteon.dto.requestDto.PostBannerDTO;
import com.lotteon.dto.requestDto.PostCouponDto;
import com.lotteon.dto.requestDto.PostReviewDto;
import com.lotteon.dto.requestDto.cartOrder.*;
import com.lotteon.dto.responseDto.GetAddressDto;
import com.lotteon.dto.responseDto.GetCouponDto;
import com.lotteon.dto.responseDto.GetMainProductDto;
import com.lotteon.dto.responseDto.GetOption1Dto;
import com.lotteon.entity.config.Banner;
import com.lotteon.entity.product.Cart;
import com.lotteon.entity.product.OrderCancleDocument;
import com.lotteon.repository.member.UserLogRepository;
import com.lotteon.repository.product.OrderCancleRepository;
import com.lotteon.service.WebSocketService;
import com.lotteon.service.member.AddressService;
import com.lotteon.service.member.UserLogService;
import com.lotteon.service.point.CouponService;
import com.lotteon.service.point.CustomerCouponService;
import com.lotteon.service.point.PointService;
import com.lotteon.service.product.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Log4j2
@RestController
@RequestMapping("/prod")
@RequiredArgsConstructor
public class ApiProductController {
    private final CustomerCouponService customerCouponService;
    private final CartService cartService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final RedisTemplate<String,Object> redisTemplate;
    private final PointService pointService;
    private final CouponService couponService;
    private final UserLogRepository userLogRepository;
    private final UserLogService userLogService;
    private final ProductOptionService productOptionService;
    private final ProductService productService;
    private final ReviewService reviewService;
    private final OrderCancleRepository orderCancleRepository;
    private final WebSocketService webSocketService;
    private final AddressService addressService;

    @GetMapping("/test/coupon")
    public void toTestCouponIssue(){

        customerCouponService.useCustCoupon();
    }

    @GetMapping("/option1")
    public ResponseEntity<?> getOption1(
            @RequestParam Long id
    ){
        List<GetOption1Dto> options = productOptionService.findByProdId(id);

        return ResponseEntity.ok(options);
    }

    @GetMapping("/option2")
    public ResponseEntity<?> getOption2(
            @RequestParam String optionValue,
            @RequestParam Long prodId
    ){
        System.out.println(optionValue);
        System.out.println(prodId);
        List<GetOption1Dto> options = productOptionService.findByOptionValue(optionValue,prodId);
        Map<String,Object> map = new HashMap<>();
        map.put("option2s",options);
        return ResponseEntity.ok().body(map);
    }

    @GetMapping("/customer/coupon")
    public ResponseEntity<?> getCouponList(
            @RequestParam Long id
    ){
        List<GetCouponDto> custCoupons = couponService.findAllSeller(id);

        return ResponseEntity.ok(custCoupons);
    }

    @PostMapping("/customer/coupon")
    public ResponseEntity<?> getCustCoupon(@RequestBody PostCouponDto dto){
        String result;

        MyUserDetails auth = (MyUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        String cacheKey = "dailyCoupon::daily_" + dto.getId() + "_" + auth.getUser().getId() +"_" + LocalDate.now();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey))) {
            return ResponseEntity.ok("이미 수령한 쿠폰입니다.");
        }
        
        String dailyCoupon = customerCouponService.postDailyCoupon(dto.getId(),auth);

        return ResponseEntity.ok(dailyCoupon);
    }

    @PostMapping("/cart")
    public ResponseEntity<?> cartInsert(@RequestBody PostCartDto postCartDto,
                                        Authentication authentication,
                                        HttpServletRequest req, HttpServletResponse resp) {
        log.info("카트 컨트롤러 접속 "+postCartDto.toString());
        Map<String, String> response = new HashMap<>();

        if(authentication==null){
            log.info("비회원임!!!");
            Cart cart = cartService.insertCartFornoAuth(req, resp);
            if(cart==null){
                response.put("status", "noCart");
            }else{
                response.put("status", "noAuth");
                cartService.insertCartItem(postCartDto,cart);
            }
            return ResponseEntity.ok().body(response);
        }
        MyUserDetails auth = (MyUserDetails) authentication.getPrincipal();
        String role = auth.getUser().getMemRole();

        if(role.equals("admin") || role.equals("seller")){
            response.put("status", "seller");
        }else{
            ResponseEntity result = cartService.insertCart(postCartDto,authentication);
            Cart cart = (Cart) result.getBody();
            ResponseEntity result2 = cartService.insertCartItem(postCartDto,cart);
            response.put("status", "customer"); // Add a default response
        }
        if(auth.getUser() != null) {
            userLogService.saveUserLog(auth.getUser().getCustomer().getId(),postCartDto.getProdId(),"cart");
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/cart")
    public ResponseEntity<Long> CartDelete(@RequestBody List<Long> cartItemIds ){
        Long deletedCartItem = cartService.deleteCartItem(cartItemIds);
        return ResponseEntity.ok(deletedCartItem);
    }

    @PostMapping("/cart/save")
    public ResponseEntity<Boolean> CartToOrder(@RequestBody List<PostCartSaveDto> selectedProducts, HttpSession session){
        log.info("선택한 카트 정보 "+selectedProducts.toString());

        if(selectedProducts.isEmpty()){
            return ResponseEntity.ok(false);
        }else {
            session.setAttribute("selectedProducts", selectedProducts);
            return ResponseEntity.ok(true);
        }
    }

    @PostMapping("/order/direct")
    public ResponseEntity<?> viewToOrder(@RequestBody PostCartSaveDto selectedProduct,
                                               HttpSession session,
                                               Authentication authentication){
        log.info("선택한 카트 정보 "+selectedProduct.toString());
        Map<String, String> response = new HashMap<>();
        if(authentication==null){
            log.info("비회원임!!!");
            response.put("status", "noAuth");
            return ResponseEntity.ok().body(response);
        }
        MyUserDetails auth = (MyUserDetails) authentication.getPrincipal();
        String role = auth.getUser().getMemRole();

        List<PostCartSaveDto> selectedProducts = new ArrayList<>();
        selectedProducts.add(selectedProduct);

        if(role.equals("admin") || role.equals("seller")){
            response.put("status", "seller");
        }else{
            session.setAttribute("selectedProducts", selectedProducts);
            response.put("status", "customer");
        }
        if(auth.getUser() != null) {
            selectedProducts.forEach(v->{
                userLogService.saveUserLog(auth.getUser().getCustomer().getId(),v.getProductId(),"order");
            });
        }
        return ResponseEntity.ok(response);

    }

    @PostMapping("/order")
    public ResponseEntity order(@RequestBody PostOrderDto postOrderDto, HttpSession session){
        log.info("컨트롤러에 들어왔나요?"+postOrderDto.toString());
        MyUserDetails auth = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //카트 세션 꺼내서 null처리하고 삭제
        List<PostCartSaveDto> selectedProducts = (List<PostCartSaveDto>) session.getAttribute("selectedProducts");
        Long couponId = postOrderDto.getOrderPointAndCouponDto().getCouponId();
        List<Long> cartItemIds = new ArrayList<>();
        for(PostCartSaveDto postCartSaveDto : selectedProducts){
            if(postCartSaveDto.getCartItemId()!=null){
                Long cartItemId = postCartSaveDto.getCartItemId();
                cartItemIds.add(cartItemId);
            }
        }
        if(!cartItemIds.isEmpty()){
            cartService.deleteCartItem(cartItemIds);
        }
        log.info("카트 아이템 아이디 세션에 저장된 거 "+cartItemIds.toString());
        session.removeAttribute("selectedProducts");


        //postOrderDto 꺼내기
        OrderDto orderDto = postOrderDto.getOrderDto();
        List<OrderItemDto> orderItemDto = postOrderDto.getOrderItemDto();


        ResponseEntity<Map<String,Object>> orderItemResult = orderItemService.insertOrderItem(orderItemDto,orderDto,session,postOrderDto.getOrderPointAndCouponDto());
        selectedProducts.forEach(v->{
            userLogService.saveUserLog(auth.getUser().getCustomer().getId(),v.getProductId(),"order");
        });

        if(postOrderDto.getOrderPointAndCouponDto().getPoints()!=0){
            pointService.usePoint2(postOrderDto.getOrderPointAndCouponDto().getPoints(),(long)orderItemResult.getBody().get("orderId"));
        }


        if(postOrderDto.getOrderPointAndCouponDto().getCouponId()!=0){
            customerCouponService.useCoupon(postOrderDto.getOrderPointAndCouponDto().getCouponId());
        }

        OrderCancleDocument orderCancleDocument = OrderCancleDocument.builder()
                .points(postOrderDto.getOrderPointAndCouponDto().getPoints())
                .custId(auth.getUser().getCustomer().getId())
                .couponId(postOrderDto.getOrderPointAndCouponDto().getCouponId())
                .orderId((long)orderItemResult.getBody().get("orderId"))
                .pointId((long)orderItemResult.getBody().get("pointId"))
                .pointUdate(LocalDateTime.now())
                .build();

        orderCancleRepository.save(orderCancleDocument);

        return orderItemResult;
    }

    @GetMapping("/main")
    public ResponseEntity<?> getMainPage(
            @RequestParam String type
    ){
        List<GetMainProductDto> products;
        Map<String,Object> map = new HashMap<>();
        if(type.equals("bestRank")){
            products = productService.findBestItem();
        } else if(type.equals("hit")){
            products = productService.findHitItem();
        } else if(type.equals("recent")){
            products = productService.findRecentItem();
        } else if(type.equals("recommend")) {
            products = productService.findRecommendItem();
        } else if(type.equals("discount")){
            products = productService.findDiscountItem();
        } else {
            products = productService.findSavePointItem();
        }
        map.put("products",products);

        return ResponseEntity.ok(map);
    }

    @PostMapping("/review")
    public ResponseEntity<?> insertReview(
            @RequestPart("jsonData") String jsonData,
            @RequestPart("reviewImg") MultipartFile reviewImg
    ){

        ObjectMapper objectMapper = new ObjectMapper();
        PostReviewDto reviewDto;
        String result;

        try {
            reviewDto = objectMapper.readValue(jsonData, PostReviewDto.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Invalid JSON Data: " + e.getMessage()));
        }

        if (reviewImg != null) {
            try {
                reviewDto.setReviewImg(reviewImg); // 이미지 파일 설정
            } catch (Exception e) {
                log.error("Error setting review image", e);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("error", "Failed to set review image: " + e.getMessage()));
            }
        } else {
            log.warn("No image uploaded for the review");
        }

        try {
            result = reviewService.addReview(reviewDto);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error inserting review", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Failed to register review: " + e.getMessage()));
        }
    }

    @GetMapping("/review/names")
    public ResponseEntity<?> findReviewNames(
            @RequestParam Long orderId
    ){
        List<GetProductNamesDto> names = productService.findReviewNames(orderId);
        Map<String,Object> map = new HashMap<>();
        map.put("names",names);
        return ResponseEntity.ok(map);
    }

    @PatchMapping("/cart/option")
    public ResponseEntity<?> patchOption(@RequestParam Long id,@RequestParam Long prod, @RequestParam int quantity){
        System.out.println(quantity);
        cartService.updateCartOption(id,prod,quantity);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/cart/quantity")
    public ResponseEntity<?> patchQuantity(@RequestParam Long cart,@RequestParam Integer quantity){
        cartService.updateQuantity(cart,quantity);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/order/address")
    public ResponseEntity<?> getDifferentAddress(){
        List<GetAddressDto> address = addressService.findAllByUid();
        return ResponseEntity.ok(address);
    }

    @GetMapping("/order/address-select")
    public ResponseEntity<?> getSelectAddress(
            @RequestParam Long id
    ){
        GetAddressDto address = addressService.findByAddrId(id);
        return ResponseEntity.ok(address);
    }

    @PostMapping("/heart")
    public ResponseEntity<?> postHeart(
            @RequestParam Long id
    ){
        String result = productService.postHeart(id);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/heart")
    public ResponseEntity<?> deleteHeart(
            @RequestParam List<Long> id
    ){
        productService.deleteHearts(id);
        return ResponseEntity.ok().build();
    }

}

package com.lotteon.controller.controller;

import com.lotteon.config.MyUserDetails;
import com.lotteon.dto.requestDto.*;
import com.lotteon.dto.responseDto.*;
import com.lotteon.entity.product.Product;
import com.lotteon.entity.product.ProductOption;
import com.lotteon.entity.product.ReviewDocu;
import com.lotteon.repository.product.ReviewDocuRepository;
import com.lotteon.service.category.CategoryProductService;
import com.lotteon.service.member.UserLogService;
import com.lotteon.service.point.CouponService;
import com.lotteon.service.product.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/prod")
@RequiredArgsConstructor
@Log4j2
public class ProductController {

    private final CategoryProductService categoryProductService;
    private final ProductService productService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final CouponService couponService;
    private final ProductDetailService productDetailService;
    private final UserLogService userLogService;
    private final RecommendationService recommendationService;
    private final ProductOptionService productOptionService;
    private final ReviewService reviewService;
    private final ReviewDocuRepository reviewDocuRepository;

    @ModelAttribute
    public void findBest(Model model) {
        List<GetMainProductDto> products = productService.findBestItem();

        model.addAttribute("best",products);
    }

    @GetMapping("/products")
    public String products(Model model, @RequestParam(value = "cate",required = false) String cate, ProductPageRequestDTO productPageRequestDTO) {
        log.info("123123123"+cate);


        ProductPageResponseDTO<PostProductDTO> products = categoryProductService.findProductCategory(cate, productPageRequestDTO);
        GetCateLocationDTO location = categoryProductService.cateLocation(Long.parseLong(cate));
        List<GetCategoryDto> category1 = categoryProductService.findCategory();
        model.addAttribute("products", products);
        model.addAttribute("location", location);
        model.addAttribute("category1", category1);
        model.addAttribute("cate", cate);

        String setSortType = productPageRequestDTO.getSort();
        model.addAttribute("setSortType", setSortType);

        return "pages/product/list";
    }


    @GetMapping("/product")
    public String product(Model model, @RequestParam(value = "prodId",required = false) long prodId, @RequestParam(value = "page",defaultValue = "0") int page) {

        PostProductDTO postProductDTO = productService.selectProduct(prodId);
        model.addAttribute("product", postProductDTO);
        List<GetCategoryDto> category1 = categoryProductService.findCategory();
        model.addAttribute("category1", category1);
        Long couponId = couponService.findCouponByProduct(prodId);
        model.addAttribute("couponId", couponId);
        PostProdDetailDTO prodDetail = productDetailService.selectProdDetail(prodId);

        Set<String> addedOptions = new HashSet<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            MyUserDetails auth = (MyUserDetails) authentication.getPrincipal();
            if (auth.getUser().getCustomer() != null) {
                userLogService.saveUserLog(auth.getUser().getCustomer().getId(), prodId, "view");
                String heart = productService.getHeart(prodId);
                model.addAttribute("heart",heart);
            } else {
                model.addAttribute("heart","none");
            }
        }
        List<Product> related = recommendationService.findRelatedProducts(prodId);
        if(related.size()>0){
            model.addAttribute("related", related);
        }
        model.addAttribute("addedOptions", addedOptions);
        model.addAttribute("prodDetail", prodDetail);
        GetCateLocationDTO location = categoryProductService.cateLocation2(prodId);
        model.addAttribute("location", location);
        List<GetOption1Dto> option1 = productOptionService.findByProdId(prodId);
        model.addAttribute("option1s", option1);
        Page<GetReviewsDto> reviews = reviewService.findAllByProdId(page,prodId);
        model.addAttribute("reviews",reviews);


        return "pages/product/view";
    }


    @GetMapping("/products/search")
    public String search(
            Model model,
            @RequestParam(value = "search",defaultValue = "0") String search,
            @RequestParam(value = "page",defaultValue = "0") int page,
            @RequestParam(value = "searchType",defaultValue = "0")String searchType,
            @RequestParam(value = "keyword",defaultValue = "0") String keyword,
            @RequestParam(value = "sortby",defaultValue = "0") String sortBy,
            @RequestParam(value = "min",defaultValue = "0") int min,
            @RequestParam(value = "max",defaultValue = "0") int max,
            @RequestParam(value = "related",defaultValue = "0") String related
    ) {
        List<GetCategoryDto> category1 = categoryProductService.findCategory();
        model.addAttribute("category1", category1);
        if (!related.equals("0")) {
            HashOperations<String, String, Integer> hashOps = redisTemplate.opsForHash();

            // 해시에서 현재 검색어의 카운트를 가져옵니다.
            Integer currentCount = hashOps.get(related, search);

            if (currentCount != null) {
                // 존재하면 카운트를 +1 증가
                hashOps.put(related, search, currentCount + 1);
            } else {
                // 존재하지 않으면 1로 설정
                hashOps.put(related, search, 1);
            }
        }
        Page<GetProductDto> products;
        if(searchType.equals("0")){
            products = productService.searchProducts(page,search,sortBy);
        } else {
            products = productService.searchProductsAndDetailSearch(page,search,sortBy,keyword,searchType,min,max);
        }

        model.addAttribute("products",products);
        model.addAttribute("page",page);
        model.addAttribute("search",search);
        model.addAttribute("totalPages",products.getTotalPages());
        model.addAttribute("keyword",keyword);
        model.addAttribute("searchType",searchType);
        model.addAttribute("sortBy",sortBy);
        model.addAttribute("totalCnt",products.getTotalElements());
        model.addAttribute("min",min);
        model.addAttribute("max",max);
        model.addAttribute("related",search);
        return "pages/product/search";
    }

    @GetMapping("/reviews")
    @ResponseBody
    public List<GetReviewsDto> getReviews(@RequestParam int page, @RequestParam int size, @RequestParam Long id) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ReviewDocu> reviewPage = reviewDocuRepository.findAllByProdIdOrderByReviewRdateDesc(id,pageRequest);
        Page<GetReviewsDto> dtos = reviewPage.map(ReviewDocu::toGetReviewsDto);
        return dtos.getContent();
    }

    @GetMapping("/hearts")
    public String getHearts(@RequestParam(value = "page",defaultValue = "0")int page,Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증되지 않은 사용자라면 로그인 페이지로 리다이렉트
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/auth/login/view";
        }
        Page<GetHeartsDto> hearts = productService.findAll(page);
        if(hearts.isEmpty()){
            model.addAttribute("noItem",true);
        } else {
            model.addAttribute("noItem",false);
            model.addAttribute("hearts",hearts);
        }
        List<GetCategoryDto> category1 = categoryProductService.findCategory();
        model.addAttribute("category1", category1);
        return "pages/product/heart";

    }

}

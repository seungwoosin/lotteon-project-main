package com.lotteon.service.point;

import com.lotteon.config.MyUserDetails;
import com.lotteon.dto.requestDto.PostCouponDto;

import com.lotteon.dto.responseDto.GetCouponDto;
import com.lotteon.entity.member.Member;
import com.lotteon.entity.member.Seller;
import com.lotteon.entity.point.Coupon;
import com.lotteon.entity.point.CustomerCoupon;
import com.lotteon.entity.product.Product;
import com.lotteon.repository.member.MemberRepository;
import com.lotteon.repository.member.SellerRepository;
import com.lotteon.repository.point.CouponRepository;
import com.lotteon.repository.point.CustomerCouponRepository;
import com.lotteon.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponService {

    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;
    private final SellerRepository sellerRepository;
    private final CustomerCouponRepository customerCouponRepository;
    private final ProductRepository productRepository;

    public void postAdminCoupon(PostCouponDto postCouponDto) {
        MyUserDetails auth = (MyUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();


        Member memId = auth.getUser();

        String couponDiscountString = postCouponDto.getCouponDiscount();
        int slashIndex = couponDiscountString.indexOf('/');
        int couponDiscount = Integer.parseInt(couponDiscountString.substring(0, slashIndex));
        String couponDiscountOption = couponDiscountString.substring(slashIndex + 1);
        Coupon coupon = Coupon.builder()
                .couponMinPrice(postCouponDto.getCouponMinPrice())
                .couponCaution(postCouponDto.getCouponCaution())
                .couponDiscount(couponDiscount)
                .couponDiscountOption(couponDiscountOption)
                .couponName(postCouponDto.getCouponName())
                .couponExpiration(postCouponDto.getCouponExpiration())
                .couponType("주문상품할인")
                .member(memId)
                .customerCouponExpiration(postCouponDto.getCustomerCouponExpiration())
                .couponState("발급중")
                .couponBannerState(0)
                .couponIssueCount(0)
                .couponUseCount(0)
                .build();

        couponRepository.save(coupon);

    }


    public Page<GetCouponDto> findAllCoupons(int page) {
        MyUserDetails auth = (MyUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        Pageable pageable = PageRequest.of(page, 5);
        Page<GetCouponDto> dtos;
        if(auth.getUser().getMemRole().equals("admin")){
            Page<Coupon> coupons = couponRepository.findAllByOrderByIdDesc(pageable);
            dtos = coupons.map(Coupon::toGetCouponDto);
        } else {
            Member member = auth.getUser();
            Page<Coupon> coupons = couponRepository.findAllByMemberOrderByIdDesc(member,pageable);
            dtos = coupons.map(Coupon::toGetCouponDto);
        }
        return dtos;
    }

    public GetCouponDto findCoupon(Long id) {
        Coupon coupon = couponRepository.findById(id).orElseThrow(()->new NoSuchElementException("Coupon with id " + id + " not found."));
        GetCouponDto dto = coupon.toGetCouponDto();
        return dto;
    }

    public void updateCouponState(Long id) {
        Coupon coupon = couponRepository.findById(id).orElseThrow(()->new NoSuchElementException("Coupon with id " + id + " not found."));
        coupon.updateCouponState();
    }


    public Page<GetCouponDto> findAllCouponsBySearch(int page, String searchType, String keyword) {
        MyUserDetails auth = (MyUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        Pageable pageable = PageRequest.of(page, 5);
        Page<GetCouponDto> dtos;
        if(auth.getUser().getMemRole().equals("admin")) {
            if (searchType.equals("id")) {
                Page<Coupon> coupons = couponRepository.findAllByIdOrderByIdDesc(Long.parseLong(keyword), pageable);
                dtos = coupons.map(Coupon::toGetCouponDto);
            } else if (searchType.equals("couponName")) {
                Page<Coupon> coupons = couponRepository.findAllByCouponNameContainingOrderByIdDesc(keyword, pageable);
                dtos = coupons.map(Coupon::toGetCouponDto);
                System.out.println(dtos);
            } else {
                Optional<Seller> seller = sellerRepository.findBySellCompany(keyword);
                if(seller.isEmpty()){
                    Page<Coupon> coupons = couponRepository.findAllByOrderByIdDesc(pageable);
                    dtos = coupons.map(Coupon::toGetCouponDto);
                    return dtos;
                }
                Member member = seller.get().getMember();

                Page<Coupon> coupons = couponRepository.findAllByMemberOrderByIdDesc(member, pageable);
                dtos = coupons.map(Coupon::toGetCouponDto);
            }
        } else {
            Member member = auth.getUser();

            if (searchType.equals("id")) {
                Page<Coupon> coupons = couponRepository.findAllByIdAndMember_SellerOrderByIdDesc(Long.parseLong(keyword),member.getSeller(), pageable);
                dtos = coupons.map(Coupon::toGetCouponDto);
            } else if (searchType.equals("couponName")) {
                Page<Coupon> coupons = couponRepository.findAllByCouponNameAndMember_SellerOrderByIdDesc(keyword,member.getSeller(), pageable);
                dtos = coupons.map(Coupon::toGetCouponDto);
            } else {
                Seller seller = sellerRepository.findByMember_MemUid(keyword).orElseThrow(()->new NoSuchElementException("Seller with id " + keyword + " not found."));

                Member member2 = seller.getMember();

                Page<Coupon> coupons = couponRepository.findAllByMember_SellerOrderByIdDesc(member2.getSeller(), pageable);
                dtos = coupons.map(Coupon::toGetCouponDto);
            }
        }
        return dtos;

    }

    public void updateCouponBannerState(Long id) {
        Coupon coupon = couponRepository.findById(id).orElseThrow(()->new NoSuchElementException("Coupon with id " + id + " not found."));
        if(coupon.getCouponBannerState()==0){
            coupon.updateCouponBannerState(1);
        } else {
            coupon.updateCouponBannerState(0);
        }
    }


    public Long findCouponByProduct(long prodId) {
        Optional<Product> product = productRepository.findById(prodId);
        Member member = product.get().getSeller().getMember();
        Optional<Coupon> coupon = couponRepository.findFirstByMember(member);
        Long couponId;
        if(coupon.isEmpty()){
            couponId = (long)0;
        } else {
            couponId = coupon.get().getId();
        }

        return couponId;
    }

    public List<GetCouponDto> findAllSeller(Long id) {
        Optional<Seller> seller = sellerRepository.findById(id);
        List<Coupon> coupons = couponRepository.findAllByMember_SellerAndCouponState(seller.get(),"발급중");
        List<GetCouponDto> dtos = coupons.stream().map(v->v.toGetCouponDto()).toList();

        return dtos;
    }
}

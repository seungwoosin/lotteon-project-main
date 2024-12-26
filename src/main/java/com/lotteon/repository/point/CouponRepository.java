package com.lotteon.repository.point;

import com.lotteon.entity.member.Customer;
import com.lotteon.entity.member.Member;
import com.lotteon.entity.member.Seller;
import com.lotteon.entity.point.Coupon;
import com.lotteon.entity.point.CustomerCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Page<Coupon> findAllByOrderByIdDesc(Pageable pageable);

    Page<Coupon> findAllByMemberOrderByIdDesc(Member member, Pageable pageable);

    Page<Coupon> findAllByCouponNameContainingOrderByIdDesc(String keyword, Pageable pageable);

    Page<Coupon> findAllByIdOrderByIdDesc(long l, Pageable pageable);

    Page<Coupon> findAllByIdAndMemberOrderByIdDesc(long l,Member member, Pageable pageable);

    Page<Coupon> findAllByCouponNameAndMemberOrderByIdDesc(String keyword, Member member, Pageable pageable);

    List<Coupon> findAllByCouponNameContaining(String keyword);

    List<Coupon> findAllByMember(Member user);

    List<Coupon> findAllByMemberAndCouponNameContaining(Member user, String keyword);

    Optional<Coupon> findFirstByMember(Member member);

    Page<Coupon> findAllByIdAndMember_SellerOrderByIdDesc(long l, Seller seller, Pageable pageable);

    Page<Coupon> findAllByCouponNameAndMember_SellerOrderByIdDesc(String keyword, Seller seller, Pageable pageable);

    Page<Coupon> findAllByMember_SellerOrderByIdDesc(Seller seller, Pageable pageable);

    List<Coupon> findAllByMember_SellerAndCouponState(Seller seller, String 발급중);
}

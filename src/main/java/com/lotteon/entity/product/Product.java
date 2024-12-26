package com.lotteon.entity.product;

import com.lotteon.dto.requestDto.GetProductDto;
import com.lotteon.dto.requestDto.GetProductNamesDto;
import com.lotteon.dto.requestDto.PostProductDTO;
import com.lotteon.dto.responseDto.GetMainProductDto;
import com.lotteon.entity.category.CategoryProduct;
import com.lotteon.entity.category.CategoryProductMapper;
import com.lotteon.entity.member.Seller;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sell_id")
    private Seller seller;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private List<CategoryProductMapper> categoryMappings = new ArrayList<>();

    @Column(name = "prod_summary")
    private String prodSummary;

    @Column(name = "prod_price")
    private Double prodPrice;

    @Column(name = "prod_discount")
    private Double prodDiscount;

    @Column(name = "prod_deli")
    private int prodDeliver;

    @Column(name = "prod_name")
    private String prodName;

    @Column(name = "prod_list_img")
    private String prodListImg;

    @Column(name = "prod_basic_img")
    private String prodBasicImg;

    @Column(name = "prod_detail_img")
    private String prodDetailImg;

    @Column(name = "prod_stock")
    private int prodStock;

    @Setter
    @Column(name = "prod_order_cnt")
    private Integer prodOrderCnt;

    @Column(name = "prod_views")
    private int prodViews;

    @Column(name = "prod_rating")
    private int prodRating;

    @Column(name = "prod_point")
    private int prodPoint;

    @Column(name = "prod_rdate")
    @CreationTimestamp
    private LocalDateTime prodRdate;

    @Column(name = "prod_review_cnt")
    private Integer prodReviewCnt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private List<ProductOption> options = new ArrayList<>();

    public GetProductDto toGetProductDto() {
        return GetProductDto.builder()
                .id(id)
                .deli(prodDeliver)
                .img(prodListImg)
                .discount(prodDiscount)
                .title(prodName)
                .price(prodPrice)
                .rating(prodRating)
                .summary(prodSummary)
                .orderCnt(prodOrderCnt)
                .reviewCnt(prodReviewCnt)
                .sell_uid(seller.getSellCompany())
                .grade(seller.getSellGrade())
                .build();
    }

    public GetMainProductDto toGetMainBestDto(){
        return GetMainProductDto.builder()
                .prodPrice(prodPrice)
                .prodDiscount(prodDiscount)
                .img(prodListImg)
                .prodName(prodName)
                .id(id)
                .rating(prodRating)
                .orderCnt(prodOrderCnt)
                .reviewCnt(prodReviewCnt)
                .type("best")
                .build();
    }

    public GetMainProductDto toGetMainHitDto(){
        String deli ;
        if(prodDeliver>0){
            deli = String.valueOf(prodDeliver);
        } else {
            deli = "무료배송";
        }
        return GetMainProductDto.builder()
                .prodPrice(prodPrice)
                .prodDiscount(prodDiscount)
                .img(prodListImg)
                .prodName(prodName)
                .id(id)
                .deli(deli)
                .rating(prodRating)
                .orderCnt(prodOrderCnt)
                .reviewCnt(prodReviewCnt)
                .type("hit")
                .build();
    }

    public void updateRating(double averageScore) {
        this.prodRating = (int)averageScore;
    }

    public void updateViewCnt() {
        this.prodViews = prodViews +1;
    }

    public void updateProduct(PostProductDTO dto) {
        this.id = dto.getId();
        this.seller = dto.getSeller();
        this.prodName = dto.getProdName();
        this.prodDetailImg = dto.getProdDetailImg();
        this.prodBasicImg = dto.getProdBasicImg();
        this.prodListImg = dto.getProdListImg();
        this.prodOrderCnt = dto.getProdOrderCnt();
        this.prodViews = dto.getProdViews();
        this.prodRating = dto.getProdRating();
        this.prodPoint = dto.getProdPoint();
        this.prodDeliver = dto.getProdDeliver();
        this.categoryMappings = dto.getCategoryMappings();
        this.prodSummary = dto.getProdSummary();
        this.prodPrice = dto.getProdPrice();
        this.prodDiscount = dto.getProdDiscount();
        this.prodStock = dto.getProdStock();
        this.options = dto.getOptions();
    }

    public void updateStock(int total){
        this.prodStock = total;
    }

    public void updateReviewCnt() {
        this.prodReviewCnt = prodReviewCnt + 1;
    }
}

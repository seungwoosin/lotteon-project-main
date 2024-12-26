package com.lotteon.entity.category;

import com.lotteon.dto.responseDto.GetCategoryDto;
import com.lotteon.dto.responseDto.TestResponseDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Entity
@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category_product")
public class CategoryProduct {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;
    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "category_level")
    private int categoryLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private CategoryProduct parent;

    @Column(name = "category_order")
    @Setter
    private Integer categoryOrder;

    @ToString.Exclude
    @OneToMany(mappedBy = "parent")
    private List<CategoryProduct> children;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<CategoryProductMapper> productMappings = new ArrayList<>();

    public TestResponseDto toDto() {
        return TestResponseDto.builder()
                .name(categoryName)
                .level(categoryLevel)
                .parent(parent.getCategoryName())
                .build();
    }

    public GetCategoryDto toGetCategoryDto(){
        return GetCategoryDto.builder()
                .name(categoryName)
                .id(categoryId)
                .build();
    }

    public void updateParent(CategoryProduct categoryProduct){
        List<CategoryProduct> children = categoryProduct.getChildren();

        this.parent = categoryProduct;
        this.categoryOrder = children.isEmpty()
                ? 1
                : children.stream()
                .max(Comparator.comparing(CategoryProduct::getCategoryOrder))
                .map(child -> child.getCategoryOrder() + 1)
                .orElse(1);
    }


    public void replaceOrder(CategoryProduct otherCategory) {
        int tempOrder = this.getCategoryOrder();

        // 현재 객체의 categoryOrder 값을 otherCategory의 값으로 설정
        this.categoryOrder = otherCategory.getCategoryOrder();

        // otherCategory의 categoryOrder 값을 임시로 저장된 값으로 설정
        otherCategory.setCategoryOrder(tempOrder);
    }
}

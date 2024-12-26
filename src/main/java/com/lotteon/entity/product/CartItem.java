package com.lotteon.entity.product;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart_item")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    @ToString.Exclude
    @Setter
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prod_id")
    @ToString.Exclude
    private Product product;

    @Column(name = "cart_item_quantity")
    private int quantity;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "prod_option_id")
//    private ProductOption productOption;

    @Setter
    @Column(name = "total_price")
    private Double totalPrice;

    @OneToMany(mappedBy = "cartItem", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CartItemOption> selectedOptions = new ArrayList<>();

    @Column(name = "option_id")
    private Long optionId;

    @Column(name = "option_curr_additional")
    private Double optionCurrAdditional;

    public void updateOption(Long id, Double additional) {
        this.optionId = id;
        this.totalPrice = ((this.totalPrice / this.quantity)-optionCurrAdditional+additional)*this.quantity;
    }

    public void setQuantity(int quantity) {
        this.totalPrice = (totalPrice/this.quantity)*quantity;
        this.quantity = quantity;
    }

    public void updateAdditional(Double additionalPrice) {
        this.optionCurrAdditional = additionalPrice;
    }

    public void insertAdditional(Long id ,Double additionalPrice) {
        this.optionCurrAdditional = additionalPrice;
        this.optionId = id;
    }
}

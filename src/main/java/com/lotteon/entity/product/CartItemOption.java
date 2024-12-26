package com.lotteon.entity.product;

import jakarta.persistence.*;
import lombok.*;

@Entity
@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart_item_option")
public class CartItemOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 다대일 관계: CartItem -> CartItemOption
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_item_id")
    @ToString.Exclude
    private CartItem cartItem;

    @Column(name = "option_id")
    private Long prodOptionId;
}

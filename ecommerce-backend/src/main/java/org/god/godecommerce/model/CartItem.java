package org.god.godecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    @NotNull
    @DecimalMin(value = "0.00", message = "Discount should not be less than 0.00")
    @DecimalMax(value = "100.00", message = "Discount cannot exceed 100%")
    @Column(precision = 19, scale = 2)
    private BigDecimal discount = BigDecimal.ZERO;

    @NotNull
    @Column(precision = 19, scale = 2)
    @DecimalMin(value = "0.00", message = "Price should be >= 0.00")
    private BigDecimal productPrice;

    @NotNull
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}

package org.god.godecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotBlank
    @Size(min = 3, message = "Product userName must contain at least 3 letters.")
    private String productName;

    @NotBlank
    private String image;

    @NotBlank
    @Size(min = 5, message = "Product userName must contain at least 5 letters.")
    private String description;

    @NotNull
    private Integer quantity;

    @NotNull
    @DecimalMin(value = "0.00", message = "Price should be >= 0.00")
    @Column(precision = 19, scale = 2)
    private BigDecimal price;

    @NotNull
    @DecimalMin(value = "0.00", message = "Discount should not be less than 0.00")
    @DecimalMax(value = "100.00", message = "Discount cannot exceed 100%")
    @Column(precision = 19, scale = 2)
    private BigDecimal discount = BigDecimal.ZERO;

    @Column(precision = 19, scale = 2)
    @DecimalMin(value = "0.00", message = "Special Price should not be less than 0.00")
    private BigDecimal specialPrice;

    public Product() {
    }

    public Product(String productName, String image, String description, Integer quantity, BigDecimal price, BigDecimal discount) {
        this.productName = productName;
        this.image = image;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
    }

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;
}

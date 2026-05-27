package org.god.godecommerce.repository;

import org.god.godecommerce.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT EXISTS (SELECT 1 FROM CartItem ci WHERE ci.product.productId = :productId AND ci.cart.cartId = :cartId)")
    boolean existsByProductIdAndCartId(@Param("productId") Long productId, @Param("cartId") Long cartId);

    @Query("SELECT ci FROM CartItem ci WHERE ci.product.productId = :productId AND ci.cart.cartId = :cartId")
    Optional<CartItem> findByProductIdAndCartId(@Param("productId") Long productId, @Param("cartId") Long cartId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem ci WHERE ci.product.productId = :productId")
    void deleteByProductId(@Param("productId") Long productId);
}

package org.god.godecommerce.repository;

import org.god.godecommerce.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderItems oi JOIN oi.product p where p.seller.userId = :userId")
    Page<Order> findOrderBySeller(@Param("userId") Long userId, Pageable pageable);
}

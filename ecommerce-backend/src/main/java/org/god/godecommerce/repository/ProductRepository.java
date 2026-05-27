package org.god.godecommerce.repository;

import org.god.godecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    Page<Product> findByProductNameContainingIgnoreCase(String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p where p.seller.userId = :sellerId")
    Page<Product> findAllByProductsBySeller(@Param("sellerId") Long sellerId,
                                            Pageable pageable);
}

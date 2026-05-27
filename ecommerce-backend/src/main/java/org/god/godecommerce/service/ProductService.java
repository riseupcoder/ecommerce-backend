package org.god.godecommerce.service;

import jakarta.validation.Valid;
import org.god.godecommerce.payload.ProductDTO;
import org.god.godecommerce.payload.ProductResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductResponse getAllProducts(Pageable pageable);

    ProductResponse getProductsByCategory(Long categoryId, Pageable pageable);

    ProductResponse getProductsByKeyword(String keyword, Pageable pageable);

    ProductDTO createProduct(Long categoryId, @Valid ProductDTO productDTO, Long sellerId);

    ProductDTO updateProduct(Long productId, ProductDTO productDTO);

    void deleteProduct(Long productId);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;

    ProductResponse getSellerProducts(Long userId, Pageable pageable);
}

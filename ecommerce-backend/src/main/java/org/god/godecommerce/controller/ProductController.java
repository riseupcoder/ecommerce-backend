package org.god.godecommerce.controller;

import jakarta.validation.Valid;
import org.god.godecommerce.config.AppConstants;
import org.god.godecommerce.payload.ProductDTO;
import org.god.godecommerce.payload.ProductResponse;
import org.god.godecommerce.security.jwt.services.UserDetailsImpl;
import org.god.godecommerce.service.ProductService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @PageableDefault(sort = AppConstants.SORT_PRODUCTS_BY) Pageable pageable) {
        ProductResponse productsDTO = productService.getAllProducts(pageable);
        return new ResponseEntity<>(productsDTO, HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(
            @PathVariable Long categoryId,
            @PageableDefault(sort = AppConstants.SORT_PRODUCTS_BY) Pageable pageable) {
        ProductResponse productsDTO = productService.getProductsByCategory(categoryId, pageable);
        return new ResponseEntity<>(productsDTO, HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(
            @PathVariable String keyword,
            @PageableDefault(sort = AppConstants.SORT_PRODUCTS_BY) Pageable pageable) {
        ProductResponse productsDTO = productService.getProductsByKeyword(keyword, pageable);
        return new ResponseEntity<>(productsDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> createProduct(
            @RequestBody @Valid ProductDTO productDTO,
            @PathVariable Long categoryId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProductDTO savedProductDTO = productService.createProduct(categoryId, productDTO, userDetails.id());
        return new ResponseEntity<>(savedProductDTO, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @PutMapping("/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long productId, @RequestBody @Valid ProductDTO productDTO) {
        ProductDTO updatedProductDTO = productService.updateProduct(productId, productDTO);
        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(
            @PathVariable Long productId,
            @RequestParam("image") MultipartFile image
    ) throws IOException {
        ProductDTO updatedProduct = productService.updateProductImage(productId, image);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>("product with productId: " + productId + " deleted successfully.", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SELLER')")
    @GetMapping("/seller/products")
    public ResponseEntity<ProductResponse> getSellerProducts(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PageableDefault() Pageable pageable
            ) {
        ProductResponse sellerProducts = productService.getSellerProducts(userDetails.id(), pageable);
        return new ResponseEntity<>(sellerProducts, HttpStatus.OK);
    }
}

package org.god.godecommerce.service;

import jakarta.transaction.Transactional;
import org.god.godecommerce.exception.ResourceNotFoundException;
import org.god.godecommerce.mapper.ProductMapper;
import org.god.godecommerce.model.Category;
import org.god.godecommerce.model.Product;
import org.god.godecommerce.model.User;
import org.god.godecommerce.payload.ProductDTO;
import org.god.godecommerce.payload.ProductResponse;
import org.god.godecommerce.repository.CartItemRepository;
import org.god.godecommerce.repository.CategoryRepository;
import org.god.godecommerce.repository.ProductRepository;
import org.god.godecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final FileService fileService;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    @Value("${project.image}")
    private String imageDir;

    public ProductServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository, ProductMapper productMapper, FileService fileService, CartItemRepository cartItemRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.fileService = fileService;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ProductResponse getAllProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductDTO> productDTOS = productPage.getContent().stream().map(productMapper::toDto).toList();

        return new ProductResponse(productDTOS, productPage.getNumber(), productPage.getSize(), productPage.getTotalElements(), productPage.getTotalPages(), productPage.isLast());
    }

    @Override
    public ProductResponse getProductsByCategory(Long categoryId, Pageable pageable) {
        categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        Page<Product> productPage = productRepository.findByCategoryId(categoryId, pageable);

        List<ProductDTO> productDTOS = productPage.getContent().stream().map(productMapper::toDto).toList();

        return new ProductResponse(productDTOS, productPage.getNumber(), productPage.getSize(), productPage.getTotalElements(), productPage.getTotalPages(), productPage.isLast());
    }

    @Override
    public ProductResponse getProductsByKeyword(String keyword, Pageable pageable) {
        Page<Product> productPage = productRepository.findByProductNameContainingIgnoreCase(keyword, pageable);

        List<ProductDTO> productDTOS = productPage.getContent().stream().map(productMapper::toDto).toList();

        return new ProductResponse(productDTOS, productPage.getNumber(), productPage.getSize(), productPage.getTotalElements(), productPage.getTotalPages(), productPage.isLast());
    }

    @Override
    @Transactional
    public ProductDTO createProduct(Long categoryId, ProductDTO productDTO, Long sellerId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        Product product = productMapper.toEntity(productDTO);
        product.setCategory(category);

        BigDecimal specialPrice = calculateSpecialPrice(product.getPrice(), product.getDiscount());
        product.setSpecialPrice(specialPrice);

        User seller = userRepository.findById(sellerId).orElseThrow();

        product.setSeller(seller);

        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        productMapper.updateEntityFromDto(productDTO, existingProduct);

        BigDecimal specialPrice = calculateSpecialPrice(existingProduct.getPrice(), existingProduct.getDiscount());
        existingProduct.setSpecialPrice(specialPrice);

        productRepository.save(existingProduct);
        return productMapper.toDto(existingProduct);
    }


    @Override
    public void deleteProduct(Long productId) {
        cartItemRepository.deleteByProductId(productId);

        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        productRepository.delete(product);
    }

    @Override
    @Transactional
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        String fileName = fileService.uploadImage(imageDir, image);

        product.setImage(fileName);
        return productMapper.toDto(product);
    }

    @Transactional
    @Override
    public ProductResponse getSellerProducts(Long sellerId, Pageable pageable) {
        Page<Product> productPage = productRepository.findAllByProductsBySeller(sellerId, pageable);

        List<ProductDTO> productDTOS = productPage.getContent().stream().map(productMapper::toDto).toList();

        return new ProductResponse(productDTOS, productPage.getNumber(), productPage.getSize(), productPage.getTotalElements(), productPage.getTotalPages(), productPage.isLast());
    }

    private BigDecimal calculateSpecialPrice(BigDecimal price, BigDecimal discount) {
        return price.subtract(price.multiply(discount).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP);
    }
}

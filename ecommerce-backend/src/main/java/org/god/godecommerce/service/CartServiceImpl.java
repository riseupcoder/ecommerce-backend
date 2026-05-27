package org.god.godecommerce.service;

import org.god.godecommerce.exception.APIException;
import org.god.godecommerce.exception.ResourceNotFoundException;
import org.god.godecommerce.mapper.CartMapper;
import org.god.godecommerce.model.*;
import org.god.godecommerce.payload.CartDTO;
import org.god.godecommerce.payload.CartResponse;
import org.god.godecommerce.repository.CartItemRepository;
import org.god.godecommerce.repository.CartRepository;
import org.god.godecommerce.repository.ProductRepository;
import org.god.godecommerce.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    public CartServiceImpl(ProductRepository productRepository, UserRepository userRepository, CartItemRepository cartItemRepository, CartRepository cartRepository, CartMapper cartMapper) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
    }

    @Override
    @Transactional
    public CartDTO addToCart(Long productId, Integer quantity, Long userId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("product", "productId", productId));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createNewCart(userId));

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setProductPrice(product.getSpecialPrice());
        cartItem.setDiscount(product.getDiscount());

        if (cartItemRepository.existsByProductIdAndCartId(productId, cart.getCartId()))
            throw new APIException("Product " + product.getProductName() + " already exists in the cart.");

        if (product.getQuantity() == 0)
            throw new APIException("Product " + product.getProductName() + " has no stock left.");

        if (quantity > product.getQuantity())
            throw new APIException(product.getProductName() + " only has " + product.getQuantity() + " items in stock but you have requested " + quantity + " items");

        cart.getCartItems().add(cartItem);
        cart.setTotalPrice(cart.getTotalPrice().add(cartItem.getProductPrice().multiply(BigDecimal.valueOf(quantity))));

        cartRepository.save(cart);

        return cartMapper.toDto(cart);
    }

    @Override
    public Cart createNewCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        Cart newCart = new Cart();
        newCart.setUser(user);
        return cartRepository.save(newCart);
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponse getAllCarts(Pageable pageable) {
        Page<Cart> cartPage = cartRepository.findAll(pageable);

        List<CartDTO> cartDTOS = cartPage.getContent().stream()
                .map(cartMapper::toDto)
                .toList();

        return new CartResponse(
                cartDTOS,
                cartPage.getNumber(),
                cartPage.getSize(),
                cartPage.getTotalElements(),
                cartPage.getTotalPages(),
                cartPage.isLast()
        );
    }

    @Override
    @Transactional
    public CartDTO getUserCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new APIException("Cart is empty add new items to the cart."));

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem c: cart.getCartItems()) {
            c.setProductPrice(c.getProduct().getSpecialPrice());
            total = total.add(c.getProduct().getSpecialPrice().multiply(BigDecimal.valueOf(c.getQuantity())));
        }

        cart.setTotalPrice(total);

        return cartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public CartDTO updateItemQuantity(Long itemId, Integer quantity, Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new APIException("Cart is empty add new items to the cart."));

        CartItem cartItem = cartItemRepository.findByProductIdAndCartId(itemId, cart.getCartId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", itemId));

        Integer productTotalQuantity = cartItem.getProduct().getQuantity();

        if (productTotalQuantity == 0)
            throw new APIException("Product " + cartItem.getProduct().getProductName() + " no stock left.");

        if (quantity > productTotalQuantity)
            throw new APIException(cartItem.getProduct().getProductName() + " only has " + productTotalQuantity + " items in stock but you have requested " + quantity + " items");

        if (quantity <= 0) {
            cart.getCartItems().remove(cartItem);
        } else {
            cartItem.setQuantity(quantity);
        }

        cart.setTotalPrice(cart.getCartItems().stream()
                .map(item -> item.getProductPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        return cartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public String deleteItemFromCart(Long itemId, Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new APIException("Cart is empty add new items to your cart."));

        CartItem cartItem = cartItemRepository.findByProductIdAndCartId(itemId, cart.getCartId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", itemId));

        String productName = cartItem.getProduct().getProductName();

        cart.getCartItems().remove(cartItem);

        cart.setTotalPrice(cart.getCartItems().stream()
                .map(item -> item.getProductPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
        );

        return "Product " + productName + " removed from the cart.";
    }
}

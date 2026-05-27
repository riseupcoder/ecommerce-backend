package org.god.godecommerce.controller;

import jakarta.validation.Valid;
import org.god.godecommerce.payload.CartDTO;
import org.god.godecommerce.payload.AddToCartRequest;
import org.god.godecommerce.payload.CartResponse;
import org.god.godecommerce.payload.UpdateCartItemRequest;
import org.god.godecommerce.security.jwt.services.UserDetailsImpl;
import org.god.godecommerce.service.CartService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/cart-item")
    public ResponseEntity<CartDTO> addToCart(
            @RequestBody AddToCartRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        CartDTO cartItem = cartService.addToCart(
                request.productId(), request.productQuantity(), userDetails.id());

        return new ResponseEntity<>(cartItem, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<CartResponse> getAllCarts(
            @PageableDefault() Pageable pageable
    ) {
        CartResponse allCarts = cartService.getAllCarts(pageable);
        return new ResponseEntity<>(allCarts, HttpStatus.OK);
    }

    @GetMapping("/cart")
    public ResponseEntity<CartDTO> getUserCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        CartDTO cartDTO = cartService.getUserCart(userDetails.id());
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @PutMapping("/cart-item/{itemId}")
    public ResponseEntity<CartDTO> updateItemQuantity(
            @PathVariable Long itemId, @Valid @RequestBody UpdateCartItemRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CartDTO updatedCartItem = cartService.updateItemQuantity(
                itemId, request.quantity(), userDetails.id());
        return new ResponseEntity<>(updatedCartItem, HttpStatus.OK);
    }

    @DeleteMapping("/cart-item/{itemId}")
    public ResponseEntity<String> deleteItemFromCart(
            @PathVariable Long itemId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String status = cartService.deleteItemFromCart(itemId, userDetails.id());
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

}

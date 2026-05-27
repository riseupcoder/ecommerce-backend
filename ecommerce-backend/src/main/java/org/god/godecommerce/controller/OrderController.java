package org.god.godecommerce.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.god.godecommerce.payload.*;
import org.god.godecommerce.security.jwt.services.UserDetailsImpl;
import org.god.godecommerce.service.OrderService;
import org.god.godecommerce.service.StripeService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final StripeService stripeService;

    public OrderController(OrderService orderService, StripeService stripeService) {
        this.orderService = orderService;
        this.stripeService = stripeService;
    }

    @PostMapping("/order")
    public ResponseEntity<OrderDTO> orderProducts(
            @RequestBody OrderRequestDTO request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        OrderDTO order = orderService.placeOrder(userDetails.email(), request);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PostMapping("/stripe-payment")
    public ResponseEntity<String> createStripeClientSecret(
            @RequestBody StripePaymentDTO stripePaymentDTO) throws StripeException {
        PaymentIntent paymentIntent = stripeService.createPaymentIntent(stripePaymentDTO);
        return new ResponseEntity<>(paymentIntent.getClientSecret(), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<OrderResponse> getAllOrders(
            @PageableDefault()Pageable pageable
            ) {
        OrderResponse allOrders = orderService.getAllOrders(pageable);
        return new ResponseEntity<>(allOrders, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @PutMapping("/order")
    public ResponseEntity<OrderDTO> updateOrderStatus(@RequestBody OrderStatusUpdateDTO request) {
        OrderDTO updatedOrder = orderService.updateOrderStatus(request);
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SELLER')")
    @GetMapping("/seller")
    public ResponseEntity<OrderResponse> getSellerOrder(
            @PageableDefault() Pageable pageable,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        OrderResponse sellerOrders = orderService.getSellerOrders(userDetails.id(), pageable);
        return new ResponseEntity<>(sellerOrders, HttpStatus.OK);
    }
}

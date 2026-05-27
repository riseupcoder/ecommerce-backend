package org.god.godecommerce.service;

import org.god.godecommerce.exception.APIException;
import org.god.godecommerce.exception.ResourceNotFoundException;
import org.god.godecommerce.mapper.OrderMapper;
import org.god.godecommerce.model.*;
import org.god.godecommerce.payload.OrderDTO;
import org.god.godecommerce.payload.OrderRequestDTO;
import org.god.godecommerce.payload.OrderResponse;
import org.god.godecommerce.payload.OrderStatusUpdateDTO;
import org.god.godecommerce.repository.*;
import org.god.godecommerce.util.AuthUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final CartRepository cartRepository;
    private final AuthUtil authUtil;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public OrderServiceImpl(CartRepository cartRepository, AuthUtil authUtil, AddressRepository addressRepository, ProductRepository productRepository, OrderMapper orderMapper, OrderRepository orderRepository, PaymentRepository paymentRepository) {
        this.cartRepository = cartRepository;
        this.authUtil = authUtil;
        this.addressRepository = addressRepository;
        this.productRepository = productRepository;
        this.orderMapper = orderMapper;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    @Override
    public OrderDTO placeOrder(String email, OrderRequestDTO request) {
        Cart cart = cartRepository.findByUserId(authUtil.loggedInUserId())
                .orElseThrow(() -> new APIException("Cart is empty add items first before ordering"));

        Address address = addressRepository.findByAddressIdAndUserId(request.addressId(), authUtil.loggedInUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", request.addressId()));

        List<OrderItem> orderItems = cart.getCartItems().stream()
                .map(orderMapper::cartItemToOrderItem)
                .toList();

        Payment payment = new Payment();
        payment.setPaymentMethod(request.paymentMethod());
        payment.setPgName(request.pgName());
        payment.setPgPaymentId(request.pgPaymentId());
        payment.setPgStatus(request.pgStatus());
        payment.setPgResponseMessage(request.pgResponseMessage());

        Order order = new Order();
        order.setEmail(email);
        order.setOrderDate(LocalDate.now());
        order.setPayment(payment);
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus(OrderStatus.PROCESSING);
        order.setAddress(address);
        order.setOrderItems(orderItems);

        payment.setOrder(order);

        // set the order items -> order id we transferred cart items to order items so its null so we are setting it now very useful for later.
        for (OrderItem orderItem: orderItems)
            orderItem.setOrder(order);

        orderRepository.save(order);
        paymentRepository.save(payment);

        // update product stock after order is set
        for (OrderItem orderItem : orderItems) {
            Product product = orderItem.getProduct();
            Integer availableStock = product.getQuantity();
            if (availableStock < orderItem.getQuantity())
                throw new APIException("Insufficient stock for product: " + product.getProductId());
            product.setQuantity(availableStock - orderItem.getQuantity());
            productRepository.save(product);
        }

        cart.getCartItems().removeAll(cart.getCartItems());

        return orderMapper.toDto(order);
    }

    @Transactional
    @Override
    public OrderResponse getAllOrders(Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAll(pageable);

        List<OrderDTO> orderDTOS = orderPage.getContent().stream()
                .map(orderMapper::toDto)
                .toList();

        return new OrderResponse(orderDTOS,
                orderPage.getNumber(),
                orderPage.getSize(),
                orderPage.getTotalElements(),
                orderPage.getTotalPages(),
                orderPage.isLast()
        );
    }

    @Transactional
    @Override
    public OrderDTO updateOrderStatus(OrderStatusUpdateDTO request) {
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", request.orderId()));

        order.setOrderStatus(request.orderStatus());
        return orderMapper.toDto(order);
    }

    @Transactional
    @Override
    public OrderResponse getSellerOrders(Long userId, Pageable pageable) {
        Page<Order> orderPage = orderRepository.findOrderBySeller(userId, pageable);
        List<OrderDTO> orderDTOS = orderPage.getContent().stream()
                .map(orderMapper::toDto)
                .toList();
        return new OrderResponse(
                orderDTOS,
                orderPage.getNumber(),
                orderPage.getSize(),
                orderPage.getTotalElements(),
                orderPage.getTotalPages(),
                orderPage.isLast()
        );
    }
}

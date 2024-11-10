package com.ntp.ecommercespringboot.service;

import com.ntp.ecommercespringboot.dto.CartDTO;
import com.ntp.ecommercespringboot.dto.OrderDTO;
import com.ntp.ecommercespringboot.exception.InsufficientStockException;
import com.ntp.ecommercespringboot.exception.ResourceNotFoundException;
import com.ntp.ecommercespringboot.mapper.CartMapper;
import com.ntp.ecommercespringboot.mapper.OrderMapper;
import com.ntp.ecommercespringboot.model.*;
import com.ntp.ecommercespringboot.repositories.OrderRepositoty;
import com.ntp.ecommercespringboot.repositories.ProductRepository;
import com.ntp.ecommercespringboot.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepositoty orderRepositoty;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private final CartService cartService;
    private final EmailService emailService;

    private final OrderMapper orderMapper;
    private final CartMapper cartMapper;

    @Transactional
    public OrderDTO createOrder(Long userId, String address, String phoneNumber) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        if (!user.isEmailConfirmation()) {
            throw new IllegalStateException("Email not confirmed. Please confirm email before placing order");
        }

        CartDTO cartDTO = cartService.getCart(userId);
        Cart cart = cartMapper.toEntity(cartDTO);

        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setPhoneNumber(phoneNumber);
        order.setStatus(Order.OrderStatus.PREPARING);
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> orderItems = createOrderItems(cart, order);
        order.setItems(orderItems);

        Order savedOrder = orderRepositoty.save(order);
        cartService.clearCart(userId);

        try {
            emailService.sendOrderConfirmation(savedOrder);
        } catch (MailException ex) {
            logger.error("Failed to send order confirmation email for order ID " + savedOrder.getId(), ex);
        }

        return orderMapper.toDTO(savedOrder);
    }

    public List<OrderItem> createOrderItems(Cart cart, Order order) {
        return cart.getItems().stream().map(cartItem -> {
            Product product = productRepository.findById(cartItem.getProduct().getId()).orElseThrow(() -> new EntityNotFoundException(("Product Not Found With ID: " + cartItem.getProduct().getId())));

            if (product.getQuantity() == null) {
                throw new IllegalStateException("Product quantity is not set for product " + product.getName());
            }

            if (product.getQuantity() < cartItem.getQuantity()) {
                throw new InsufficientStockException("Not enough stock for product " + product.getName());
            }

            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            return new OrderItem(null, cartItem.getQuantity(), product.getPrice(), order, product);
        }).collect(Collectors.toList());
    }

    public List<OrderDTO> getAllOrders() {
        return orderMapper.toDTOs(orderRepositoty.findAll());
    }

    public List<OrderDTO> getUserOrders(Long userId) {
        return orderMapper.toDTOs(orderRepositoty.findByUserId(userId));
    }

    public OrderDTO updateOrderStatus(Long orderId, Order.OrderStatus orderStatus) {
        Order order = orderRepositoty.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order Not Found"));
        order.setStatus(orderStatus);
        Order updateOrder = orderRepositoty.save(order);
        return orderMapper.toDTO(updateOrder);
    }
}

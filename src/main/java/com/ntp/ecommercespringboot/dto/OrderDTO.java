package com.ntp.ecommercespringboot.dto;

import com.ntp.ecommercespringboot.model.Order;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;

    private Long userId;

    @NotBlank(message = "Address is required")
    private String address;

    private Order.OrderStatus status;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    private LocalDateTime createAt;

    private List<OrderItemDTO> orderItems;
}

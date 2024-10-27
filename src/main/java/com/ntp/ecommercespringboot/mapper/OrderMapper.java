package com.ntp.ecommercespringboot.mapper;

import com.ntp.ecommercespringboot.dto.OrderDTO;
import com.ntp.ecommercespringboot.dto.OrderItemDTO;
import com.ntp.ecommercespringboot.dto.ProductDTO;
import com.ntp.ecommercespringboot.model.Order;
import com.ntp.ecommercespringboot.model.OrderItem;
import com.ntp.ecommercespringboot.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderItems", source = "items")
    OrderDTO toDTO(Order order);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "items", source = "orderItems")
    Order toEntity(OrderDTO orderDTO);

    List<OrderDTO> toDTOs(List<Order> orders);

    List<Order> toEntities(List<OrderDTO> orderDTOS);

    @Mapping(target = "productId", source = "product.id")
    OrderItemDTO toOrderItemDTO(OrderItem orderItem);

    @Mapping(target = "product.id", source = "productId")
    OrderItem toOrderItemEntity(OrderItemDTO orderItemDTO);

    List<OrderItemDTO> toOrderItemDTOs(List<OrderItem> orderItem);

    List<OrderItem> toOrderItemEntities(List<OrderItemDTO> orderItemDTO);

}

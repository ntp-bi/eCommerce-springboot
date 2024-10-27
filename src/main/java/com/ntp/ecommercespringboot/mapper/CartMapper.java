package com.ntp.ecommercespringboot.mapper;

import com.ntp.ecommercespringboot.dto.CartDTO;
import com.ntp.ecommercespringboot.dto.CartItemDTO;
import com.ntp.ecommercespringboot.dto.ProductDTO;
import com.ntp.ecommercespringboot.model.Cart;
import com.ntp.ecommercespringboot.model.CartItem;
import com.ntp.ecommercespringboot.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "userId", source = "user.id")
    CartDTO toDTO(Cart Cart);

    @Mapping(target = "user.id", source = "userId")
    Cart toEntity(CartDTO cartDTO);

    @Mapping(target = "productId", source = "product.id")
    CartItemDTO toDTO(CartItem cartItem);

    @Mapping(target = "product.id", source = "productId")
    CartItem toEntity(CartItemDTO cartItemDTO);
}

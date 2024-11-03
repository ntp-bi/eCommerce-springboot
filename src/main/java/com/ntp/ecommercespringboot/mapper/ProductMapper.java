package com.ntp.ecommercespringboot.mapper;

import com.ntp.ecommercespringboot.dto.CommentDTO;
import com.ntp.ecommercespringboot.dto.ProductDTO;
import com.ntp.ecommercespringboot.model.Comment;
import com.ntp.ecommercespringboot.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "image", source = "image")
    ProductDTO toDTO(Product product);

    @Mapping(target = "image", source = "image")
    Product toEntity(ProductDTO productDTO);

    @Mapping(target = "userId", source = "user.id")
    CommentDTO toDTO(Comment comment);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "product", ignore = true)
    Comment toEntity(CommentDTO commentDTO);
}

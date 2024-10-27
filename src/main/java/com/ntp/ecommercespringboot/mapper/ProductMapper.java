package com.ntp.ecommercespringboot.mapper;

import com.ntp.ecommercespringboot.dto.CommentDTO;
import com.ntp.ecommercespringboot.dto.ProductDTO;
import com.ntp.ecommercespringboot.model.Comment;
import com.ntp.ecommercespringboot.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDTO(Product product);

    Product toEntity(ProductDTO productDTO);

    CommentDTO toDTO(Comment comment);

    Comment toEntity(CommentDTO commentDTO);
}
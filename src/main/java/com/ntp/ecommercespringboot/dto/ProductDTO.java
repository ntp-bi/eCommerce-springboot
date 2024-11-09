package com.ntp.ecommercespringboot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDTO {
    private Long id;

    @NotBlank(message = "Product name is required")
    private String name;

    @Positive(message = "Cannot be negative")
    private BigDecimal price;

    @PositiveOrZero(message = "Cannot be negative")
    private Integer quantity;

    private String image;

    @NotBlank(message = "Product description is required")
    private String description;

    private List<CommentDTO> comments;
}

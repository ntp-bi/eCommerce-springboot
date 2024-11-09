package com.ntp.ecommercespringboot.service;

import com.ntp.ecommercespringboot.dto.CommentDTO;
import com.ntp.ecommercespringboot.exception.ResourceNotFoundException;
import com.ntp.ecommercespringboot.mapper.CommentMapper;
import com.ntp.ecommercespringboot.model.Comment;
import com.ntp.ecommercespringboot.model.Product;
import com.ntp.ecommercespringboot.model.User;
import com.ntp.ecommercespringboot.repositories.CommentRepositoty;
import com.ntp.ecommercespringboot.repositories.ProductRepository;
import com.ntp.ecommercespringboot.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepositoty commentRepositoty;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private final CommentMapper commentMapper;

    public CommentDTO addComment(Long productId, Long userId, CommentDTO commentDTO) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Comment comment = commentMapper.toEntity(commentDTO);
        comment.setProduct(product);
        comment.setUser(user);
        Comment saveComment = commentRepositoty.save(comment);

        return commentMapper.toDTO(saveComment);
    }

    public List<CommentDTO> getCommentsByProduct(Long productId) {
        List<Comment> comments = commentRepositoty.findByProductId(productId);
        return comments.stream()
                .map(commentMapper::toDTO)
                .collect(Collectors.toList());
    }
}

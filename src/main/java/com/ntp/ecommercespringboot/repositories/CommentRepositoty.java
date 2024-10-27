package com.ntp.ecommercespringboot.repositories;

import com.ntp.ecommercespringboot.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepositoty extends JpaRepository<Comment, Long> {
    List<Comment> findByProductId(Long productId);
}

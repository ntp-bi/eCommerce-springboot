package com.ntp.ecommercespringboot.repositories;

import com.ntp.ecommercespringboot.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepositoty extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}

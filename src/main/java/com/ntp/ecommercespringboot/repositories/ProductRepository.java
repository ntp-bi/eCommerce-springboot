package com.ntp.ecommercespringboot.repositories;

import com.ntp.ecommercespringboot.dto.ProductListDTO;
import com.ntp.ecommercespringboot.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT new com.ntp.ecommercespringboot.dto.ProductListDTO(p.id, p.name, p.price, p.quantity, p.image, p.description) FROM Product p")
    Page<ProductListDTO> findAllWithoutComments(Pageable pageable);
}

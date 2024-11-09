package com.ntp.ecommercespringboot.service;

import com.ntp.ecommercespringboot.dto.CartDTO;
import com.ntp.ecommercespringboot.exception.InsufficientStockException;
import com.ntp.ecommercespringboot.exception.ResourceNotFoundException;
import com.ntp.ecommercespringboot.mapper.CartMapper;
import com.ntp.ecommercespringboot.model.Cart;
import com.ntp.ecommercespringboot.model.CartItem;
import com.ntp.ecommercespringboot.model.Product;
import com.ntp.ecommercespringboot.model.User;
import com.ntp.ecommercespringboot.repositories.CartRepository;
import com.ntp.ecommercespringboot.repositories.ProductRepository;
import com.ntp.ecommercespringboot.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private final CartMapper cartMapper;

    public CartDTO addToCart(Long productId, Long userId, Integer quantity) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (product.getQuantity() < quantity) {
            throw new InsufficientStockException("Not enough available products to add to cart");
        }

        Cart cart = cartRepository.findByUserId(userId).orElse(new Cart(null, new ArrayList<>(), user));
        Optional<CartItem> existingCartItem = cart.getItems().stream().filter(cartItem -> cartItem.getProduct().getId().equals(productId)).findFirst();
        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            CartItem cartItem = new CartItem(null, quantity, cart, product);
            cart.getItems().add(cartItem);
        }

        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toDTO(savedCart);
    }

    public CartDTO getCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        return cartMapper.toDTO(cart);
    }

    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}

package com.example.productservice.Repository;

import com.example.productservice.Model_SelfService.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Override
    Optional<Product> findById(UUID uuid);
    void deleteById(UUID id);
}

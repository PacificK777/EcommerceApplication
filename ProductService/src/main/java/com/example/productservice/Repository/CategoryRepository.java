package com.example.productservice.Repository;

import com.example.productservice.Model_SelfService.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    @Override
    Optional<Category> findById(UUID uuid);

    Category save(Category category);

    Optional<Category> findByName(String category);
}

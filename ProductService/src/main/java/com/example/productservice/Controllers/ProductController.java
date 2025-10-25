package com.example.productservice.Controllers;

import com.example.productservice.Commons.AuthUtils;
import com.example.productservice.DTOs.GenericProductDTO;
import com.example.productservice.DTOs.UserDTO;
import com.example.productservice.Exceptions.InvalidTokenException;
import com.example.productservice.Exceptions.ProductNotFoundException;
import com.example.productservice.Services.ProductService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final AuthUtils authUtils;

    ProductController(@Qualifier("selfProductServiceImpl") ProductService productService,
                      AuthUtils authUtils) {
        this.productService = productService;
        this.authUtils = authUtils;
    }

    @GetMapping
    public ResponseEntity<List<GenericProductDTO>> getAllProducts(
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestHeader("Authorization") String token) throws InvalidTokenException {

        UserDTO userDTO = authUtils.validateTokens(token);
        if (userDTO == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (limit != null) {
            return ResponseEntity.ok(productService.getLimitedProducts(limit));
        }
        if (sort != null) {
            return ResponseEntity.ok(productService.getSortedProducts(sort));
        }
        return ResponseEntity.ok(productService.getAllProducts());
    }


    @GetMapping("/{id}")
    public GenericProductDTO getProductById(@PathVariable("id") String id) throws ProductNotFoundException {
        if (id == null || id.isEmpty()) {
            throw new ProductNotFoundException("Product not found for id: " + id);
        }
        try {
            UUID uuid = UUID.fromString(id);
            return productService.getProductById(uuid);
        } catch (IllegalArgumentException e) {
            throw new ProductNotFoundException("Product not found for id: " + id);
        }
    }

    @DeleteMapping("/{id}")
    public String deleteProductById(@PathVariable("id") String id) throws ProductNotFoundException {
        if (id == null || id.isEmpty()) { // Check for null or empty ID
            throw new IllegalArgumentException("Product ID cannot be null or empty");
        }
        return productService.deleteProductById(UUID.fromString(id));
    }

    @PutMapping("/{id}")
    public GenericProductDTO updateProductById(@PathVariable("id") String id,
                                               @RequestBody GenericProductDTO genericProductDTO) throws ProductNotFoundException {
        if (id == null || id.isEmpty()) { // Check for null or empty ID
            throw new IllegalArgumentException("Product ID cannot be null or empty");
        }
        return productService.updateProductById(UUID.fromString(id), genericProductDTO);
    }

    @PostMapping
    public GenericProductDTO createProduct(@RequestBody GenericProductDTO genericProductDTO) {
        return productService.createProduct(genericProductDTO);
    }

    @GetMapping("/categories")
    public List<String> getAllCategories() {
        return productService.getAllCategories();
    }

    @GetMapping("/category/{category}")
    public List<GenericProductDTO> getProductsForSpecificCategory(@PathVariable("category") String category) {
        return productService.getProductsForSpecificCategory(category);
    }
}
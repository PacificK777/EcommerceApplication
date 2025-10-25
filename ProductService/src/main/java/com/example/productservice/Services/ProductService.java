package com.example.productservice.Services;

import com.example.productservice.DTOs.GenericProductDTO;
import com.example.productservice.Exceptions.ProductNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ProductService {

    List<GenericProductDTO> getAllProducts();

    GenericProductDTO getProductById(UUID id) throws ProductNotFoundException;

    String deleteProductById(UUID id) throws ProductNotFoundException;

    GenericProductDTO updateProductById(UUID id, GenericProductDTO genericProductDTO) throws ProductNotFoundException;

    GenericProductDTO createProduct(GenericProductDTO genericProductDTO);

    List<String> getAllCategories();

    List<GenericProductDTO> getProductsForSpecificCategory(String category);

    List<GenericProductDTO> getLimitedProducts(int limit);

    List<GenericProductDTO> getSortedProducts(String sort);
}

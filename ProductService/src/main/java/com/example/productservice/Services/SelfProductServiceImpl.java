package com.example.productservice.Services;

import com.example.productservice.DTOs.GenericProductDTO;
import com.example.productservice.Exceptions.ProductNotFoundException;
import com.example.productservice.Model_SelfService.Category;
import com.example.productservice.Model_SelfService.Product;
import com.example.productservice.Repository.CategoryRepository;
import com.example.productservice.Repository.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service("selfProductServiceImpl")
public class SelfProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    SelfProductServiceImpl(ProductRepository productRepository,
                           CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    //*********CREATE A PRODUCT API***********
    @Override
    public GenericProductDTO createProduct(GenericProductDTO genericProductDTO) {
        // Convert DTO to entity
        Product product = new Product();
        if (genericProductDTO.getTitle() == null || genericProductDTO.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Product title is required");
        }
        product.setTitle(genericProductDTO.getTitle());
        product.setDescription(genericProductDTO.getDescription());
        if (genericProductDTO.getPrice() == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }
        product.setPrice(genericProductDTO.getPrice());
        product.setImage(genericProductDTO.getImage());

        // Check if category exists by name
        Category category = categoryRepository.findByName(genericProductDTO.getCategory())
                .orElseGet(() -> {
                    // Create new category
                    Category newCategory = new Category();
                    newCategory.setName(genericProductDTO.getCategory());
                    return categoryRepository.save(newCategory);
                });
        product.setCategory(category);

        // Save entity to database
        Product savedProduct = productRepository.save(product);

        // Convert entity back to DTO because now the ID has been generated that we need to show in result

        return getGenericProductDTO(savedProduct);
    }


    //*********GET ALL PRODUCT API***********
    @Override
    public List<GenericProductDTO> getAllProducts() {
        List<Product> productList = productRepository.findAll();
        List<GenericProductDTO> productDTOS = new ArrayList<>();
        for (Product product : productList) {
            productDTOS.add(getGenericProductDTO(product));
        }
        return productDTOS;
    }


    //*********GET PRODUCT WITH ID API***********
    @Override
    public GenericProductDTO getProductById(UUID id) throws ProductNotFoundException {
        Product product = productRepository.findById(id).
                orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        return getGenericProductDTO(product);
    }


    //*********DELETE PRODUCT WITH ID API***********
    @Override
    public String deleteProductById(UUID id) throws ProductNotFoundException {
        Product product = productRepository.findById(id).
                orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        product.setIsDeleted(true);
        productRepository.save(product);
        return ("Product successfully deleted for id: " + id);
    }


    //*********GET ALL CATEGORIES API***********
    @Override
    public List<String> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<String> categoryList = new ArrayList<>();
        for (Category category : categories) {
            categoryList.add(category.getName());
        }
        return categoryList;
    }

    //*********GET PRODUCTS FOR SPECIFIC CATEGORY API***********
    @Override
    public List<GenericProductDTO> getProductsForSpecificCategory(String category) {
        List<Product> productList = productRepository.findAll();
        List<GenericProductDTO> productDTOList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getCategory().getName().equalsIgnoreCase(category)) {
                productDTOList.add(getGenericProductDTO(product));
            }
        }
        return productDTOList;
    }


    //*********GET LIMITED PRODUCTS API***********
    @Override
    public List<GenericProductDTO> getLimitedProducts(int limit) {
        List<Product> productList = productRepository.findAll(PageRequest.of(0, limit)).getContent();
        List<GenericProductDTO> productDTOS = new ArrayList<>();
        for (Product product : productList) {
            productDTOS.add(getGenericProductDTO(product));
        }
        return productDTOS;
    }


    //*********GET SORTED PRODUCTS API***********
    @Override
    public List<GenericProductDTO> getSortedProducts(String sort) {
        List<Product> productList;

        if ("asc".equalsIgnoreCase(sort)) {
            productList = productRepository.findAll(Sort.by(Sort.Direction.ASC, "title"));
        } else if ("desc".equalsIgnoreCase(sort)) {
            productList = productRepository.findAll(Sort.by(Sort.Direction.DESC, "title"));
        } else {
            productList = productRepository.findAll(); // No sorting if input is invalid
        }

        List<GenericProductDTO> productDTOS = new ArrayList<>();
        for (Product product : productList) {
            productDTOS.add(getGenericProductDTO(product));
        }
        return productDTOS;
    }


    //*********UPDATE PRODUCT WITH ID API***********
    @Override
    public GenericProductDTO updateProductById(UUID id, GenericProductDTO genericProductDTO) throws ProductNotFoundException {
        // Retrieve the existing product by ID
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        // Update the product fields with values from the DTO
        existingProduct.setTitle(genericProductDTO.getTitle());
        existingProduct.setDescription(genericProductDTO.getDescription());
        existingProduct.setPrice(genericProductDTO.getPrice());
        existingProduct.setImage(genericProductDTO.getImage());

        // Check if category exists by name
        Category category = categoryRepository.findByName(genericProductDTO.getCategory())
                .orElseGet(() -> {
                    // Create new category
                    Category newCategory = new Category();
                    newCategory.setName(genericProductDTO.getCategory());
                    return categoryRepository.save(newCategory);
                });
        existingProduct.setCategory(category);

        // Save the updated product to the repository
        Product updatedProduct = productRepository.save(existingProduct);

        // Convert the updated product entity back to DTO
        return getGenericProductDTO(updatedProduct);
    }


    private static GenericProductDTO getGenericProductDTO(Product savedProduct) {
        GenericProductDTO savedProductDTO = new GenericProductDTO();
        savedProductDTO.setId(savedProduct.getId().toString());
        savedProductDTO.setTitle(savedProduct.getTitle());
        savedProductDTO.setDescription(savedProduct.getDescription());
        savedProductDTO.setPrice(savedProduct.getPrice());
        savedProductDTO.setCategory(savedProduct.getCategory().getName());
        savedProductDTO.setImage(savedProduct.getImage());
        return savedProductDTO;
    }
}

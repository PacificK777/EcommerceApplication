package com.example.productservice.Services;

import com.example.productservice.DTOs.GenericProductDTO;
import com.example.productservice.Model_SelfService.Category;
import com.example.productservice.Model_SelfService.Product;
import com.example.productservice.Exceptions.ProductNotFoundException;
import com.example.productservice.Repository.CategoryRepository;
import com.example.productservice.Repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SelfProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private SelfProductServiceImpl selfProductService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProduct() {
        // Test case 1: Create a new product successfully
        GenericProductDTO productDTO = new GenericProductDTO();
        productDTO.setTitle("New Product");
        productDTO.setDescription("Description");
        productDTO.setPrice(100.0);
        productDTO.setCategory("Category");
        productDTO.setImage("image.jpg");

        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setTitle("New Product");
        product.setDescription("Description");
        product.setPrice(100.0);
        product.setCategory(new Category("Category"));
        product.setImage("image.jpg");

        when(productRepository.save(any(Product.class))).thenReturn(product);

        GenericProductDTO result = selfProductService.createProduct(productDTO);
        assertNotNull(result);
        assertEquals("New Product", result.getTitle());

        // Test case 2: Create a product with missing fields
        GenericProductDTO incompleteProductDTO = new GenericProductDTO();
        incompleteProductDTO.setTitle("Incomplete Product");

        assertThrows(IllegalArgumentException.class, () -> {
            selfProductService.createProduct(incompleteProductDTO);
        });

        // Test case 3: Create a product with existing category
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(new Category("Category")));

        GenericProductDTO resultWithExistingCategory = selfProductService.createProduct(productDTO);
        assertNotNull(resultWithExistingCategory);
        assertEquals("Category", resultWithExistingCategory.getCategory());
    }

    @Test
    void getAllProducts() {
        // Test case 1: Get all products successfully
        Product product1 = new Product();
        product1.setId(UUID.randomUUID());
        product1.setTitle("New Product");
        product1.setDescription("Description");
        product1.setPrice(100.0);
        product1.setCategory(new Category("Category"));
        product1.setImage("image.jpg");

        Product product2 = new Product();
        product2.setId(UUID.randomUUID());
        product2.setTitle("New Product");
        product2.setDescription("Description");
        product2.setPrice(100.0);
        product2.setCategory(new Category("Category"));
        product2.setImage("image.jpg");

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));
        List<GenericProductDTO> result = selfProductService.getAllProducts();
        assertEquals(2, result.size());

        // Test case 2: Get all products when no products exist
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        List<GenericProductDTO> emptyResult = selfProductService.getAllProducts();
        assertTrue(emptyResult.isEmpty());

    }

    @Test
    void getProductById() throws ProductNotFoundException {
        Product product = new Product();
        product.setTitle("New Product");
        product.setDescription("Description");
        product.setPrice(100.0);
        product.setCategory(new Category("Category"));
        product.setImage("image.jpg");
        UUID id = UUID.randomUUID();
        product.setId(id);

        // Test case 1: Get product by ID successfully
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        GenericProductDTO result = selfProductService.getProductById(id);
        assertNotNull(result, "Product should not be null");
        assertNotNull(result);
        assertEquals(id.toString(), result.getId());

        // Test case 2: Get product by non-existing ID
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            selfProductService.getProductById(id);
        });
    }

    @Test
    void deleteProductById() throws ProductNotFoundException {
        Product product = new Product();
        product.setTitle("New Product");
        product.setDescription("Description");
        product.setPrice(100.0);
        product.setCategory(new Category("Category"));
        product.setImage("image.jpg");
        UUID id = UUID.randomUUID();
        product.setId(id);

        // Test case 1: Delete product by ID successfully
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        String result = selfProductService.deleteProductById(id);
        assertEquals("Product successfully deleted for id: " + id, result);

        // Test case 2: Delete product by non-existing ID
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            selfProductService.deleteProductById(id);
        });
    }

    @Test
    void getAllCategories() {
        // Test case 1: Get all categories successfully
        List<Category> categories = Arrays.asList(new Category("Category1"), new Category("Category2"));
        when(categoryRepository.findAll()).thenReturn(categories);

        List<String> result = selfProductService.getAllCategories();
        assertEquals(2, result.size());
        assertTrue(result.contains("Category1"));
        assertTrue(result.contains("Category2"));

        // Test case 2: Get all categories when no categories exist
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        List<String> emptyResult = selfProductService.getAllCategories();
        assertTrue(emptyResult.isEmpty());

        // Test case 3: Get all categories with some categories having no name
        Category unnamedCategory = new Category();
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(new Category("Category1"), unnamedCategory));

        List<String> resultWithUnnamed = selfProductService.getAllCategories();
        assertEquals(2, resultWithUnnamed.size());
        assertTrue(resultWithUnnamed.contains("Category1"));
    }

    @Test
    void getProductsForSpecificCategory() {
        String category = "Category";

        // Test case 1: Get products for specific category successfully
        Product product = new Product();
        product.setTitle("New Product");
        product.setDescription("Description");
        product.setPrice(100.0);
        product.setCategory(new Category("Category"));
        product.setImage("image.jpg");
        UUID id = UUID.randomUUID();
        product.setId(id);
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));

        List<GenericProductDTO> result = selfProductService.getProductsForSpecificCategory(category);
        assertEquals(1, result.size());

        // Test case 2: Get products for non-existing category
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        List<GenericProductDTO> emptyResult = selfProductService.getProductsForSpecificCategory(category);
        assertTrue(emptyResult.isEmpty());

        // Test case 3: Get products for specific category with mixed categories
        Product otherCategoryProduct = new Product();
        otherCategoryProduct.setCategory(new Category("OtherCategory"));
        when(productRepository.findAll()).thenReturn(Arrays.asList(product, otherCategoryProduct));

        List<GenericProductDTO> mixedResult = selfProductService.getProductsForSpecificCategory(category);
        assertEquals(1, mixedResult.size());
    }

    @Test
    void getLimitedProducts() {
        int limit = 2;

        // Test case 1: Get limited products successfully
        Product product1 = new Product();
        product1.setTitle("New Product");
        product1.setDescription("Description");
        product1.setPrice(100.0);
        product1.setCategory(new Category("Category"));
        product1.setImage("image.jpg");
        UUID id1 = UUID.randomUUID();
        product1.setId(id1);

        Product product2 = new Product();
        product2.setTitle("New Product");
        product2.setDescription("Description");
        product2.setPrice(100.0);
        product2.setCategory(new Category("Category"));
        product2.setImage("image.jpg");
        UUID id2 = UUID.randomUUID();
        product2.setId(id2);
        List<Product> products = Arrays.asList(product1, product2);
        when(productRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(products));

        List<GenericProductDTO> result = selfProductService.getLimitedProducts(limit);
        assertEquals(2, result.size());

        // Test case 2: Get limited products when no products exist
        when(productRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(Collections.emptyList()));

        List<GenericProductDTO> emptyResult = selfProductService.getLimitedProducts(limit);
        assertTrue(emptyResult.isEmpty());

        // Test case 3: Get limited products with fewer products than limit
        List<Product> fewerProducts = Arrays.asList(product1);
        when(productRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(fewerProducts));

        List<GenericProductDTO> fewerResult = selfProductService.getLimitedProducts(limit);
        assertEquals(1, fewerResult.size());
    }

    @Test
    void getSortedProducts() {
        String sort = "asc";

        // Test case 1: Get sorted products in ascending order
        Product product1 = new Product();
        product1.setTitle("New Product");
        product1.setDescription("Description");
        product1.setPrice(100.0);
        product1.setCategory(new Category("Category"));
        product1.setImage("image.jpg");
        UUID id1 = UUID.randomUUID();
        product1.setId(id1);

        Product product2 = new Product();
        product2.setTitle("New Product");
        product2.setDescription("Description");
        product2.setPrice(100.0);
        product2.setCategory(new Category("Category"));
        product2.setImage("image.jpg");
        UUID id2 = UUID.randomUUID();
        product2.setId(id2);
        List<Product> products = Arrays.asList(product1, product2);
        when(productRepository.findAll(any(Sort.class))).thenReturn(products);

        List<GenericProductDTO> result = selfProductService.getSortedProducts(sort);
        assertEquals(2, result.size());

        // Test case 2: Get sorted products in descending order
        sort = "desc";
        when(productRepository.findAll(any(Sort.class))).thenReturn(products);

        List<GenericProductDTO> descResult = selfProductService.getSortedProducts(sort);
        assertEquals(2, descResult.size());

        // Test case 3: Get sorted products with invalid sort parameter
        sort = "invalid";
        when(productRepository.findAll()).thenReturn(products);

        List<GenericProductDTO> invalidResult = selfProductService.getSortedProducts(sort);
        assertEquals(2, invalidResult.size());
    }

    @Test
    void updateProductById() throws ProductNotFoundException {
        UUID id = UUID.randomUUID();
        GenericProductDTO productDTO = new GenericProductDTO();
        productDTO.setTitle("Updated Product");
        productDTO.setDescription("Updated Description");
        productDTO.setPrice(200.0);
        productDTO.setCategory("Updated Category");
        productDTO.setImage("updated_image.jpg");

        Product existingProduct = new Product();
        existingProduct.setId(id);

        // Test case 1: Update product by ID successfully
        when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(new Category("Updated Category")));

        GenericProductDTO result = selfProductService.updateProductById(id, productDTO);
        assertNotNull(result);
        assertEquals("Updated Product", result.getTitle());

        // Test case 2: Update product by non-existing ID
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            selfProductService.updateProductById(id, productDTO);
        });

        // Test case 3: Update product by ID with new category
        when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(new Category("New Category"));

        GenericProductDTO resultWithNewCategory = selfProductService.updateProductById(id, productDTO);
        assertNotNull(resultWithNewCategory);
        assertEquals("New Category", resultWithNewCategory.getCategory());
    }
}
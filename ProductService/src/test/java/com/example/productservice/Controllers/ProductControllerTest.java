//// Test cases for ProductControllerTest.java
//
//package com.example.productservice.Controllers;
//
//import com.example.productservice.DTOs.GenericProductDTO;
//import com.example.productservice.Exceptions.ProductNotFoundException;
//import com.example.productservice.Services.ProductService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class ProductControllerTest {
//
//    @Mock
//    private ProductService productService;
//
//    @InjectMocks
//    private ProductController productController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void getAllProducts_withLimit() {
//        List<GenericProductDTO> products = Arrays.asList(new GenericProductDTO(), new GenericProductDTO());
//        when(productService.getLimitedProducts(2)).thenReturn(products);
//
//        List<GenericProductDTO> result = productController.getAllProducts(2, null);
//
//        assertEquals(2, result.size());
//        verify(productService, times(1)).getLimitedProducts(2);
//    }
//
//    @Test
//    void getAllProducts_withSort() {
//        List<GenericProductDTO> products = Arrays.asList(new GenericProductDTO(), new GenericProductDTO());
//        when(productService.getSortedProducts("price:asc")).thenReturn(products);
//
//        List<GenericProductDTO> result = productController.getAllProducts(null, "price:asc");
//
//        assertEquals(2, result.size());
//        verify(productService, times(1)).getSortedProducts("price:asc");
//    }
//
//    @Test
//    void getAllProducts_withoutParams() {
//        List<GenericProductDTO> products = Arrays.asList(new GenericProductDTO(), new GenericProductDTO());
//        when(productService.getAllProducts()).thenReturn(products);
//
//        List<GenericProductDTO> result = productController.getAllProducts(null, null);
//
//        assertEquals(2, result.size());
//        verify(productService, times(1)).getAllProducts();
//    }
//
//    @Test
//    void getProductById_validId() throws ProductNotFoundException {
//        UUID id = UUID.randomUUID();
//        GenericProductDTO product = new GenericProductDTO();
//        when(productService.getProductById(id)).thenReturn(product);
//
//        GenericProductDTO result = productController.getProductById(id.toString());
//
//        assertNotNull(result);
//        verify(productService, times(1)).getProductById(id);
//    }
//
//    @Test
//    void getProductById_invalidId() throws ProductNotFoundException {
//        UUID id = UUID.randomUUID();
//        when(productService.getProductById(id)).thenThrow(new ProductNotFoundException("Product not found"));
//
//        assertThrows(ProductNotFoundException.class, () -> productController.getProductById(id.toString()));
//        verify(productService, times(1)).getProductById(id);
//    }
//
//
//    @Test
//    void getProductById_nullId() {
//        assertThrows(IllegalArgumentException.class, () -> productController.getProductById(null));
//    }
//
//
//    @Test
//    void deleteProductById_validId() throws ProductNotFoundException {
//        UUID id = UUID.randomUUID();
//        when(productService.deleteProductById(id)).thenReturn("Product successfully marked as deleted for id: " + id);
//
//        String result = productController.deleteProductById(id.toString());
//
//        assertEquals("Product successfully marked as deleted for id: " + id, result);
//        verify(productService, times(1)).deleteProductById(id);
//    }
//
//    @Test
//    void deleteProductById_invalidId() throws ProductNotFoundException {
//        UUID id = UUID.randomUUID();
//        when(productService.deleteProductById(id)).thenThrow(new ProductNotFoundException("Product not found"));
//
//        assertThrows(ProductNotFoundException.class, () -> productController.deleteProductById(id.toString()));
//        verify(productService, times(1)).deleteProductById(id);
//    }
//
//    @Test
//    void deleteProductById_nullId() {
//        assertThrows(IllegalArgumentException.class, () -> productController.deleteProductById(null));
//    }
//
//    @Test
//    void updateProductById_validId() throws ProductNotFoundException {
//        UUID id = UUID.randomUUID();
//        GenericProductDTO productDTO = new GenericProductDTO();
//        when(productService.updateProductById(id, productDTO)).thenReturn(productDTO);
//
//        GenericProductDTO result = productController.updateProductById(id.toString(), productDTO);
//
//        assertNotNull(result);
//        verify(productService, times(1)).updateProductById(id, productDTO);
//    }
//
//    @Test
//    void updateProductById_invalidId() throws ProductNotFoundException {
//        UUID id = UUID.randomUUID();
//        GenericProductDTO productDTO = new GenericProductDTO();
//        when(productService.updateProductById(id, productDTO)).thenThrow(new ProductNotFoundException("Product not found"));
//
//        assertThrows(ProductNotFoundException.class, () -> productController.updateProductById(id.toString(), productDTO));
//        verify(productService, times(1)).updateProductById(id, productDTO);
//    }
//
//
//    @Test
//    void updateProductById_nullId() throws ProductNotFoundException {
//        GenericProductDTO productDTO = new GenericProductDTO();
//        assertThrows(IllegalArgumentException.class, () -> productController.updateProductById(null, productDTO));
//        verify(productService, never()).updateProductById(any(), eq(productDTO));
//    }
//
//    @Test
//    void createProduct() {
//        GenericProductDTO productDTO = new GenericProductDTO();
//        when(productService.createProduct(productDTO)).thenReturn(productDTO);
//
//        GenericProductDTO result = productController.createProduct(productDTO);
//
//        assertNotNull(result);
//        verify(productService, times(1)).createProduct(productDTO);
//    }
//
//    @Test
//    void getAllCategories() {
//        List<String> categories = Arrays.asList("Electronics", "Books");
//        when(productService.getAllCategories()).thenReturn(categories);
//
//        List<String> result = productController.getAllCategories();
//
//        assertEquals(2, result.size());
//        verify(productService, times(1)).getAllCategories();
//    }
//
//    @Test
//    void getProductsForSpecificCategory() {
//        String category = "Electronics";
//        List<GenericProductDTO> products = Arrays.asList(new GenericProductDTO(), new GenericProductDTO());
//        when(productService.getProductsForSpecificCategory(category)).thenReturn(products);
//
//        List<GenericProductDTO> result = productController.getProductsForSpecificCategory(category);
//
//        assertEquals(2, result.size());
//        verify(productService, times(1)).getProductsForSpecificCategory(category);
//    }
//}
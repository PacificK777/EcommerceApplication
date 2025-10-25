import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;//package com.example.productservice.Services;
//
//import com.example.productservice.DTOs.FakeStoreProductDTO;
//import com.example.productservice.DTOs.GenericProductDTO;
//import com.example.productservice.Exceptions.ProductNotFoundException;
//import org.springframework.boot.web.client.RestTemplateBuilder;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RequestCallback;
//import org.springframework.web.client.ResponseExtractor;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service("FakeStoreProductServiceImpl")
//public class FakeStoreProductServiceImpl implements ProductService {
//
//    private RestTemplateBuilder restTemplateBuilder;
//
//    private final String getSpecificProductURL = "https://fakestoreapi.com/products/{id}";
//    private final String genericProductURL = "https://fakestoreapi.com/products";
//
//    FakeStoreProductServiceImpl(RestTemplateBuilder restTemplateBuilder) {
//        this.restTemplateBuilder = restTemplateBuilder;
//    }
//
//    private static GenericProductDTO convertToGenericProductDTO(FakeStoreProductDTO fakeStoreProductDTO) {
//        GenericProductDTO genericProductDTO = new GenericProductDTO();
//        genericProductDTO.setCategory(fakeStoreProductDTO.getCategory());
//        genericProductDTO.setDescription(fakeStoreProductDTO.getDescription());
//        genericProductDTO.setId(fakeStoreProductDTO.getId());
//        genericProductDTO.setPrice(fakeStoreProductDTO.getPrice());
//        genericProductDTO.setImage(fakeStoreProductDTO.getImage());
//        genericProductDTO.setTitle(fakeStoreProductDTO.getTitle());
//        genericProductDTO.setRating(fakeStoreProductDTO.getRating());
//        return genericProductDTO;
//    }
//
//    @Override
//    public GenericProductDTO getProductById(Long id) throws ProductNotFoundException {
//        RestTemplate restTemplate = restTemplateBuilder.build();
//        ResponseEntity<FakeStoreProductDTO> responseEntity = restTemplate.getForEntity(getSpecificProductURL, FakeStoreProductDTO.class, id);
//        FakeStoreProductDTO product = responseEntity.getBody();
//        if (product == null) {
//            throw new ProductNotFoundException("Product with id " + id + " not found");
//        }
//        return convertToGenericProductDTO(product);
//    }
//
//    @Override
//    public List<GenericProductDTO> getAllProducts() {
//        RestTemplate restTemplate = restTemplateBuilder.build();
//        ResponseEntity<FakeStoreProductDTO[]> responseEntity = restTemplate.getForEntity(genericProductURL, FakeStoreProductDTO[].class);
//        List<GenericProductDTO> result = new ArrayList<>();
//        List<FakeStoreProductDTO> fakeStoreProductDTOS = List.of(responseEntity.getBody());
//        for (FakeStoreProductDTO fakeStoreProductDTO : fakeStoreProductDTOS) {
//            result.add(convertToGenericProductDTO(fakeStoreProductDTO));
//        }
//        return result;
//    }
//
//    @Override
//    public GenericProductDTO deleteProductById(Long id) throws ProductNotFoundException {
//        RestTemplate restTemplate = restTemplateBuilder.build();
//        RequestCallback requestCallback = restTemplate.acceptHeaderRequestCallback(FakeStoreProductDTO.class);
//        ResponseExtractor<ResponseEntity<FakeStoreProductDTO>> responseExtractor = restTemplate.responseEntityExtractor(FakeStoreProductDTO.class);
//        ResponseEntity<FakeStoreProductDTO> responseEntity = restTemplate.execute(getSpecificProductURL, HttpMethod.DELETE, requestCallback, responseExtractor, id);
//        FakeStoreProductDTO product = responseEntity.getBody();
//        if (product == null) {
//            throw new ProductNotFoundException("Product with id " + id + " not found");
//        }
//        return convertToGenericProductDTO(product);
//    }
//
//    @Override
//    public GenericProductDTO updateProductById(Long id, GenericProductDTO genericProductDTO) throws ProductNotFoundException {
//        RestTemplate restTemplate = restTemplateBuilder.build();
//
//        // Step 1: Get the existing product from FakeStore
//        ResponseEntity<FakeStoreProductDTO> responseEntity =
//                restTemplate.getForEntity(getSpecificProductURL, FakeStoreProductDTO.class, id);
//        FakeStoreProductDTO existingProduct = responseEntity.getBody();
//
//        // Check if the existing product is null
//        if (existingProduct == null) {
//            throw new ProductNotFoundException("Product not found with id: " + id);
//        }
//
//        // Step 2: Edit the fields
//        existingProduct.setTitle(genericProductDTO.getTitle());
//        existingProduct.setPrice(genericProductDTO.getPrice());
//        existingProduct.setCategory(genericProductDTO.getCategory());
//        existingProduct.setDescription(genericProductDTO.getDescription());
//        existingProduct.setImage(genericProductDTO.getImage());
//
//        // Step 3: Update the product
//        restTemplate.put(getSpecificProductURL, existingProduct, id);
//
//        // Step 4: Return the final product in response
//        return convertToGenericProductDTO(existingProduct);
//    }
//
//    @Override
//    public GenericProductDTO createProduct(GenericProductDTO genericProductDTO) {
//        RestTemplate restTemplate = restTemplateBuilder.build();
//        ResponseEntity<FakeStoreProductDTO> responseEntity = restTemplate.postForEntity(genericProductURL, genericProductDTO, FakeStoreProductDTO.class);
//        return convertToGenericProductDTO(responseEntity.getBody());
//    }
//
//    @Override
//    public List<String> getAllCategories() {
//        RestTemplate restTemplate = restTemplateBuilder.build();
//        ResponseEntity<FakeStoreProductDTO[]> responseEntity = restTemplate.getForEntity(genericProductURL, FakeStoreProductDTO[].class);
//        List<FakeStoreProductDTO> fakeStoreProductDTOS = List.of(responseEntity.getBody());
//        List<String> categories = new ArrayList<>();
//        for (FakeStoreProductDTO product : fakeStoreProductDTOS) {
//            if (!categories.contains(product.getCategory())) {
//                categories.add(product.getCategory());
//            }
//        }
//        return categories;
//    }
//
//    @Override
//    public List<GenericProductDTO> getProductsForSpecificCategory(String category) {
//        RestTemplate restTemplate = restTemplateBuilder.build();
//        ResponseEntity<FakeStoreProductDTO[]> responseEntity =
//                restTemplate.getForEntity(genericProductURL, FakeStoreProductDTO[].class);
//        List<FakeStoreProductDTO> fakeStoreProductDTOS = List.of(responseEntity.getBody());
//        List<GenericProductDTO> productsForCategory = new ArrayList<>();
//        for (FakeStoreProductDTO product : fakeStoreProductDTOS) {
//            if (product.getCategory().equalsIgnoreCase(category)) {
//                productsForCategory.add(convertToGenericProductDTO(product));
//            }
//        }
//        return productsForCategory;
//    }
//
//    @Override
//    public List<GenericProductDTO> getLimitedProducts(int limit) {
//        RestTemplate restTemplate = restTemplateBuilder.build();
//        String urlWithLimit = genericProductURL + "?limit=" + limit;
//        ResponseEntity<FakeStoreProductDTO[]> responseEntity = restTemplate.getForEntity(urlWithLimit, FakeStoreProductDTO[].class);
//        List<FakeStoreProductDTO> fakeStoreProductDTOS = List.of(responseEntity.getBody());
//        List<GenericProductDTO> limitedProducts = new ArrayList<>();
//        for (FakeStoreProductDTO product : fakeStoreProductDTOS) {
//            limitedProducts.add(convertToGenericProductDTO(product));
//        }
//        return limitedProducts;
//    }
//
//    @Override
//    public List<GenericProductDTO> getSortedProducts(String sort) {
//        RestTemplate restTemplate = restTemplateBuilder.build();
//        String urlWithSort = genericProductURL + "?sort=" + sort;
//        ResponseEntity<FakeStoreProductDTO[]> responseEntity = restTemplate.getForEntity(urlWithSort, FakeStoreProductDTO[].class);
//        List<FakeStoreProductDTO> fakeStoreProductDTOS = List.of(responseEntity.getBody());
//        List<GenericProductDTO> sortedProducts = new ArrayList<>();
//        for (FakeStoreProductDTO product : fakeStoreProductDTOS) {
//            sortedProducts.add(convertToGenericProductDTO(product));
//        }
//        return sortedProducts;
//    }
//}

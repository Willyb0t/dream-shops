package com.willyb0t.dreamshops.service.product;

import com.willyb0t.dreamshops.model.Product;
import com.willyb0t.dreamshops.requests.AddProductRequest;
import com.willyb0t.dreamshops.requests.ProductUpdateRequest;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest request);
    Product getProductById(long id);
    void deleteProductById(long id);
    Product updateProduct(ProductUpdateRequest request, Long ProductId);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category, String brand);
    List<Product> getProductByName(String productName);
    List<Product> getProductsByBrandAndName(String brand, String name);
    Long countProductsByBrandAndName(String brand, String name);
}

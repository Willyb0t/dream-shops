package com.willyb0t.dreamshops.service.product;

import com.willyb0t.dreamshops.exception.ProductNotFoundException;
import com.willyb0t.dreamshops.model.Category;
import com.willyb0t.dreamshops.model.Product;
import com.willyb0t.dreamshops.repository.CategoryRepository;
import com.willyb0t.dreamshops.repository.ProductRepository;
import com.willyb0t.dreamshops.requests.AddProductRequest;
import com.willyb0t.dreamshops.requests.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    /**
     * @param request
     * @return
     */
    @Override
    public Product addProduct(AddProductRequest request) {
        //check if the category is found in the DB
        //If yes, set it as the new product category
        //If no, then save it as a new category
        //The set as the new product category
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() ->{
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return productRepository.save(createProduct(request,category));
    }

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }
    /**
     * @param id 
     * @return
     */
    @Override
    public Product getProductById(long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    /**
     * @param id 
     */
    @Override
    public void deleteProductById(long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete,
                () -> {throw new ProductNotFoundException("Product not found");});
    }

    /**
     * @param request
     * @param ProductId
     */
    @Override
    public Product updateProduct(ProductUpdateRequest request, Long ProductId) {
        return productRepository.findById(ProductId)
                .map(existingProduct -> updateExistingProducts(existingProduct, request))
                .map(productRepository :: save)
                .orElseThrow(()-> new ProductNotFoundException("Product not found"));
    }

    private Product updateExistingProducts(Product existingProduct, ProductUpdateRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }
    /**
     * @return 
     */
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * @param category 
     * @return
     */
    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    /**
     * @param brand 
     * @return
     */
    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    /**
     * @param category 
     * @param brand
     * @return
     */
    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    /**
     * @param productName 
     * @return
     */
    @Override
    public List<Product> getProductByName(String productName) {
        return productRepository.findByName(productName);
    }

    /**
     * @param brand 
     * @param name
     * @return
     */
    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    /**
     * @param brand 
     * @param name
     * @return
     */
    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }
}

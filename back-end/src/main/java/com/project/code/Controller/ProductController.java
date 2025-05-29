package com.project.code.Controller;

import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Service.ServiceClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController @RequestMapping("/product")
public class ProductController {
    @Autowired private InventoryRepository inventoryRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private ServiceClass serviceClass;

    @PostMapping
    public Map<String, String> addProduct(@RequestBody Product product) {
        String result;
        try {
            if (!serviceClass.validateProduct(product)) {
                result = String.format("Product '%s' not found", product.getName());
            } else {
                productRepository.save(product);
                result = "Product saved";
            }
        } catch (Exception e) {
            result = String.format("%s: %s", e.getClass().getName(), e.getMessage());
        }
        return Map.of("message", result);
    }

    @GetMapping("/product/{id}")
    public Map<String, Object> getProductbyId(@PathVariable Long id) {
        return Map.of("products", productRepository.findById(id).orElse(null));
    }

    @PutMapping
    public Map<String, String> updateProduct(@RequestBody Product product) {
        productRepository.save(product);
        return Map.of("message", "Product updated");
    }

    @GetMapping("/category/{name}/{category}")
    public Map<String, Object> filterbyCategoryProduct(@PathVariable String name, @PathVariable String category) {
        List<Product> products;
        if ((category == null || category.isEmpty()) && name != null) {
            products = productRepository.findProductBySubName(name);
        } else if (category != null && name == null) {
            products = productRepository.findByCategory(category);
        } else {
            products = productRepository.findProductBySubNameAndCategory(name, category);
        }
        return Map.of("products", products);
    }

    @GetMapping
    public Map<String, Object> listProduct() {
        return Map.of("products", productRepository.findAll());
    }

    @GetMapping("filter/{category}/{storeId}")
    public Map<String, Object> getProductbyCategoryAndStoreId(
            @PathVariable String category, @PathVariable Long storeId) {
        return Map.of("product", productRepository.findProductsByCategory(storeId, category));
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteProduct(@PathVariable Long id) {
        String result;
        if (serviceClass.ValidateProductId(id)) {
            inventoryRepository.deleteByProductId(id);
            productRepository.deleteById(id);
            result = "Product deleted";
        } else {
            result = String.format("Product '%d' not found", id);
        }
        return Map.of("message", result);
    }

    @GetMapping("/searchProduct/{name}")
    public Map<String, Object> searchProduct(@PathVariable String name) {
        return Map.of("products", productRepository.findProductBySubName(name));
    }
}

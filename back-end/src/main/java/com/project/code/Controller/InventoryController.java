package com.project.code.Controller;

import com.project.code.Model.*;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Service.ServiceClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController @RequestMapping("/inventory")
public class InventoryController {
    @Autowired private ProductRepository productRepository;
    @Autowired private InventoryRepository inventoryRepository;
    @Autowired private ServiceClass serviceClass;

    /** Update Inventory, if Product is known (exists) and Inventory exists */
    @PutMapping
    public Map<String, String> updateInventory(@RequestBody CombinedRequest cr) {
        String result;
        try {
            Product p = Objects.requireNonNull(cr.getProduct(), "Product cannot be null");
            Inventory i = Objects.requireNonNull(cr.getInventory(), "Inventory cannot be null");
            if (!serviceClass.validateProduct(p)) {
                result = String.format("Product '%s' not found", p.getName());
            } else if (!serviceClass.validateInventory(i)) {
                result = "No data available";
            } else {
                int toStock = i.getStockLevel();
                i = inventoryRepository.findByProductIdandStoreId(p.getId(), i.getStore().getId());
                i.setStockLevel(i.getStockLevel() + toStock);
                inventoryRepository.save(i);
                result = "Successfully updated product";
            }
        } catch (Exception e) {
            result = String.format("%s: %s", e.getClass().getName(), e.getMessage());
        }
        return Map.of("message", result);
    }

    /**
     *  Save a new {@link Inventory} entry if it doesn't exist.
     * @param inventory to save
     * @return {@code "message": "success" | "already exists"}
     */
    @PostMapping
    public Map<String, String> saveInventory(@RequestBody Inventory inventory) {
        String result;
        try {
            if (!serviceClass.validateInventory(inventory))
                result = "inventory already exists";
            else {
                inventoryRepository.save(inventory);
                result = "inventory saved successfully";
            }
        } catch (Exception e) {
            result = String.format("%s: %s", e.getClass().getName(), e.getMessage());
        }
        return Map.of("message", result);
    }

    /**
     * Get all products for a specific store
     * @param storeId
     * @return
     */
    @GetMapping("/{storeid}")
    public Map<String, Object> getAllProducts(@PathVariable Long storeId) {
        return Map.of("products", productRepository.findProductsByStoreId(storeId));
    }

    /**
     * Find products by category and name
     * @param category
     * @param name
     * @param storeId
     * @return {@code "product":List<Product>}
     */
    @GetMapping("filter/{category}/{name}/{storeId}")
    public Map<String, Object> getProductName(
            @PathVariable String category,
            @PathVariable String name,
            @PathVariable Long storeId
    ) {
        java.util.List<Product> results;
        if (category == null && name != null) {
            results = productRepository.findByNameLike(storeId, name);
        } else if (category != null && name == null) {
            results = productRepository.findByCategoryAndStoreId(storeId, category);
        } else {
            results = productRepository.findByNameAndCategory(storeId, category, name);
        }
        return Map.of("product", results);
    }

    /**
     * Searches for products that match the `name` in the specified store.
     * @param name  {@code Product.name} substring
     * @param storeId
     * @return {@code "product":List<Product>}
     */
    @GetMapping("search/{name}/{storeId}")
    public Map<String, Object> searchProduct(@PathVariable String name, @PathVariable Long storeId) {
        return Map.of("product", productRepository.findByNameLike(storeId, name));
    }

    @DeleteMapping("/{id}")
    public Map<String, String> removeProduct(@PathVariable Long id) {
        var op = productRepository.findById(id);
        String deleteResult;
        if (op.isPresent()) {
            Product p = op.get();
            productRepository.delete(p);
            inventoryRepository.deleteByProductId(p.getId());
            deleteResult = "delete success";
        } else {
            deleteResult = "not found. Delete failed";
        }
        return Map.of("message", deleteResult);
    }

    @GetMapping("validate/{qty}/{storeId}/{prodId}")
    public boolean validateQuantity(@PathVariable Integer qty, @PathVariable Long storeId, @PathVariable Long prodId) {
      return inventoryRepository.findByProductIdandStoreId(prodId, storeId).getStockLevel() >= qty;
    }
}

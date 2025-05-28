package com.project.code.Service;

import com.project.code.Model.*;
import com.project.code.Repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceClass {
    @Autowired private ProductRepository productRepository;
    @Autowired private InventoryRepository inventoryRepository;

    /**
     * Checks if an inventory record exists for a given product and store combination.</br>
     * Use this method to ensure there's no duplicate inventory entry for a product-store pair.
     * If a product is already associated with the store, it will return <code>false</code>,
     * meaning no new inventory can be added.
     * @param inventory
     * @return <code>false</code> if inventory exists, otherwise <code>true</code>
     */
    public boolean validateInventory(Inventory inventory) {
        return inventoryRepository.findByProductIdandStoreId(
                inventory.getProduct().getId(), inventory.getStore().getId()) == null;
    }

    /**
     * Checks if a product exists by its name.
     * Expand this validation to check for other attributes e.g. SKU as needed
     * @param product
     * @return <code>false</code> if a product with the same name exists, otherwise <code>true</code>
     */
    public boolean validateProduct(Product product) {
            return productRepository.findByName(product.getName()) == null;
    }

    /** Checks if a product exists by given ID.</br>
     Before attempting to create an order or any other action, you can use
     this method to validate the existence of a product with a given ID.
     * @param id
     * @return <code>false</code> if the product does not exist with the given ID, otherwise <code>true</code>
     */
    public boolean ValidateProductId(long id) {
        return productRepository.findById(id).isPresent();
    }

    /**
     * Fetches the full inventory record for a given product and store combination.
     * Use when updating stock levels or performing any inventory-related checks.
     * @param in an {@link Inventory} with correct {@link Product} and {@link Store} IDs
     * @return the full Inventory entity
     */
    public Inventory getInventoryId(Inventory in) {
        return inventoryRepository.findByProductIdandStoreId(in.getProduct().getId(), in.getStore().getId());
    }
}

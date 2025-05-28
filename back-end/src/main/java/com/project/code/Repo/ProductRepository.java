package com.project.code.Repo;

import com.project.code.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAll();
    List<Product> findByCategory(String category);
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    Product findByid(Long id); // ugly hack to not clash with Optional<T> getById
    Product findBySku(String sku);
    Product findByName(String name);

    @Query("SELECT i.product FROM Inventory i WHERE i.store.id = :storeId" +
            " AND i.product.name LIKE LOWER(CONCAT('%', :pname, '%'))")
    List<Product> findByNameLike(Long storeId, String pName);

    @Query("SELECT i.product FROM Inventory i WHERE i.store.id = :storeId" +
            " AND LOWER(i.product.name) LIKE LOWER(CONCAT('%', :pName, '%'))" +
            " AND i.product.category = :category")
    List<Product> findByNameAndCategory(Long storeId, String pName, String category);

    @Query("SELECT i.product FROM Inventory i WHERE i.store.id = :storeId" +
            " AND i.product.category = :category")
    List<Product> findByCategoryAndStoreId(Long storeId, String category);

    @Query("SELECT i FROM Product i" +
            " WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :subName, '%'))")
    List<Product> findProductBySubName(String subName);

    @Query("SELECT i.product FROM Inventory i WHERE i.store.id = :storeId")
    List<Product> findProductsByStoreId(Long storeId);

    @Query("SELECT i.product FROM Inventory i" +
            " WHERE i.product.category = :category and i.store.id = :storeId")
    List<Product> findProductsByCategory(Long storeId, String category);

    @Query("SELECT i FROM Product i" +
            " WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :subName, '%'))" +
            " AND i.category = :category")
    List<Product> findProductBySubNameAndCategory(String subName, String category);
}

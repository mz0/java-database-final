package com.project.code.Repo;

import com.project.code.Model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findById(Long id);

    @Query("SELECT s FROM Store s" +
            " WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :pName, '%'))")
    List<Store> findBySubName(String pName);
}

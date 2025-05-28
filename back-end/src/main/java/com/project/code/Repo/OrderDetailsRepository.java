package com.project.code.Repo;

import com.project.code.Model.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
// no custom methods are required for this repository,
// the default CRUD operations (save, delete, update, findById, etc.) ready available
}


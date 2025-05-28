package com.project.code.Repo;

import com.project.code.Model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
// allows the repository to perform operations like save, delete, update, and find.
    Customer findByEmail(String email);
    // FIXME pointless Override org.springframework.data.repository.CrudRepository<T, ID>
    Optional<Customer> findById(Long id);

// create other query methods as needed, like
// findByPhone(String phone), List<Customer> findByName(String name);
}

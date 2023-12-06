package com.db.coffeestore9.product.repository;

import com.db.coffeestore9.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String name);

    Product findBySeq(Long seq);

}

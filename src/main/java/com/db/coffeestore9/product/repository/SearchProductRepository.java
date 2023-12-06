package com.db.coffeestore9.product.repository;

import com.db.coffeestore9.product.common.Category;
import com.db.coffeestore9.product.domain.Product;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchProductRepository extends CrudRepository<Product, Long> {

  List<Product> findProductByNameContaining(String keyword);

  List<Product> findProductByCategory(Category category);

  List<Product> findProductByNameContainingAndCategory(String keyword, Category category);
}

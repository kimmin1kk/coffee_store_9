package com.db.coffeestore9.order.repository;

import com.db.coffeestore9.order.domain.OrderContent;
import com.db.coffeestore9.order.domain.Orders;
import com.db.coffeestore9.product.domain.Product;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderContentRepository extends CrudRepository<OrderContent, Long> {
    Optional<OrderContent> findByOrdersAndProduct(Orders orders, Product product);

}

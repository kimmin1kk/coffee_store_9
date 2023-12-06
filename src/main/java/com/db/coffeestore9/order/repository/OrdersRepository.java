package com.db.coffeestore9.order.repository;

import com.db.coffeestore9.order.domain.Orders;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
    Orders findBySeq(Long seq);

    List<Orders> findOrdersListByUserUsername(String username);

}

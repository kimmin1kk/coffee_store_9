package com.db.coffeestore9.order.service;

import com.db.coffeestore9.order.common.OrderPageForm;
import com.db.coffeestore9.order.domain.OrderContent;
import com.db.coffeestore9.order.domain.Orders;
import com.db.coffeestore9.order.repository.OrdersRepository;
import com.db.coffeestore9.product.repository.ProductRepository;
import com.db.coffeestore9.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrdersService {

  private final OrdersRepository ordersRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;


  /**
   * checkCart로 먼저 주문이 가능한 상태인지 확인 processOrder에서 값이 정상인지 확인
   */
  @Transactional
  public void confirmOrder(OrderPageForm orderPageForm, Orders orders) {
    if (checkQuantityValidation(orders.getOrderContentList())) {
      updateProductCount(orders.getOrderContentList());
      processOrder(orders);
      orders.confirmOrder(orderPageForm);
    } else {
      throw new IllegalArgumentException("Error: 재고 부족으로 인한 상품 주문 불가");
    }
  }

  /**
   * isOrdered == True인 장바구니 리스트를 찾는 로직.
   *
   * @param username (Principal.getName())
   * @return List<Orders> orderedCarts
   */
  public List<Orders> getOrderedCarts(String username) {

    return userRepository.findByUsername(username).getOrdersList().stream()
        .filter(Orders::isOrdered)
        .toList();
  }

  /**
   * ordered가 True 인 주문 리스트 반환
   *
   * @return List<Orders>
   */
  public List<Orders> getAllOrderList() {
    return ordersRepository.findAll().stream()
        .filter(Orders::isOrdered)
        .toList();

  }

  /**
   * Param으로 Orders를 받아 검사하는 로직 confirmOrder 메서드 내부에서 사용
   */
  private void processOrder(Orders orders) {
    orders.getOrderContentList().stream()
        .map(OrderContent::getProduct)
        .forEach(productRepository::save);
  }

  private boolean checkQuantityValidation(List<OrderContent> list) {
    return list.stream()
        .allMatch(s -> s.getProduct().getCount() > s.getCount());
  }

  private void updateProductCount(List<OrderContent> list) {
    list.forEach(s -> s.getProduct().minusCount(s.getCount()));
  }

}

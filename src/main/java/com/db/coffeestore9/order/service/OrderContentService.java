package com.db.coffeestore9.order.service;

import com.db.coffeestore9.order.domain.OrderContent;
import com.db.coffeestore9.order.domain.Orders;
import com.db.coffeestore9.order.repository.OrderContentRepository;
import com.db.coffeestore9.order.repository.OrdersRepository;
import com.db.coffeestore9.product.repository.ProductRepository;
import com.db.coffeestore9.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderContentService {

  private final OrdersRepository ordersRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final OrderContentRepository orderContentRepository;


  /**
   * 장바구니 구매용 Orders 리스트를 가져와서 isOrdered == false && isInstant == false인 Orders의 Seq로 찾는 로직
   *
   * @param username
   * @return 장바구니
   */
  public Orders findOrders(String username) {
    return ordersRepository.findOrdersListByUserUsername(username).stream()
        .filter(findCart -> !findCart.isOrdered() && !findCart.isInstant())
        .findFirst()
        .orElse(null);
  }

  /**
   * 장바구니 구매용 조건이 맞으면 장바구니 생성 기존에 주문내역이 있는 유저 = 마지막 장바구니 isOrdered == True && isInstant == false 일
   * 경우 새로 생성 처음 주문하는 유저 = OrdersList.isEmpty -> 새로 생성
   */
  public void getOrders(String username) {
    boolean check = true;
    var user = userRepository.findByUsername(username);

    if (!user.getOrdersList().isEmpty()) { //장바구니가 이미 있엇으면 이걸로 ㅇㅇ
      for (Orders orders : user.getOrdersList()) {
        if (!orders.isInstant()) {
          check = orders.isOrdered();
        }
      }
      if (check) {
        createOrders(username);
      }
    }
    if (user.getOrdersList().isEmpty()) { //첫 장바구니 생성
      createOrders(username);
    }
  }

  /**
   * 장바구니 구매용 장바구니 생성
   *
   * @param username
   */
  public void createOrders(String username) {
    var user = userRepository.findByUsername(username);
    var orders = new Orders(user);
    ordersRepository.save(orders);
    log.info("OrderSerivce -> create Cart : OK  Cart = " + orders);
  }



  /**
   * 장바구니 구매용
   *
   * @param username
   * @return 합계액
   */
  public int findTotalPrice(String username) {
    var orders = findOrders(username);
    List<OrderContent> orderContentList = orders.getOrderContentList();
    if (!orderContentList.isEmpty()) {
      return orderContentList.stream()
          .mapToInt(i -> i.getProduct().getPrice() * i.getCount())
          .sum();
    } else {
      return 0;
    }

  }

  /**
   * 장바구니 구매용
   *
   * @param seq      상품 PK
   * @param username
   * @param count    구매하려는 상품 수
   */
  @Transactional
  public void addProductToCart(long seq, String username, int count) {
    getOrders(username);
    var orders = findOrders(username);
    productRepository.findById(seq)
        .ifPresent(product -> orderContentRepository.findByOrdersAndProduct(orders, product)
            .ifPresentOrElse(orderContent -> orderContent.addCount(count),
                () -> orderContentRepository.save(new OrderContent(orders, product, count))));
  }

}

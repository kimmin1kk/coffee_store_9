package com.db.coffeestore9.order.service;

import com.db.coffeestore9.global.common.Grade;
import com.db.coffeestore9.group.domain.GroupCard;
import com.db.coffeestore9.group.repository.GroupCardRepository;
import com.db.coffeestore9.group.service.PointService;
import com.db.coffeestore9.order.common.OrderPageForm;
import com.db.coffeestore9.order.common.PaymentMethod;
import com.db.coffeestore9.order.domain.OrderContent;
import com.db.coffeestore9.order.domain.Orders;
import com.db.coffeestore9.order.repository.OrdersRepository;
import com.db.coffeestore9.user.domain.GroupUser;
import com.db.coffeestore9.user.domain.User;
import com.db.coffeestore9.user.repository.GroupUserRepository;
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
  private final GroupCardRepository groupCardRepository;
  private final GroupUserRepository groupUserRepository;

  private final PointService pointService;

  /**
   * checkCountValidation으로 먼저 주문이 가능한 상태인지 확인한 후 Orders 업데이트, 그 후 업데이트된 정보를 이용해 GroupPoint 결제 혹은
   * GroupCard 결제
   */
  @Transactional
  public void confirmOrder(OrderPageForm orderPageForm, Orders orders) {
    if (checkCountValidation(orders.getOrderContentList())) {

      updateProductCount(orders.getOrderContentList());
      orders.confirmOrder(orderPageForm);

      if (orderPageForm.paymentMethod() == PaymentMethod.GROUP_POINT
          || orderPageForm.paymentMethod() == PaymentMethod.GROUP_CARD) {
        groupAvailableCheck(orders.getUser().getUsername());

        if (orderPageForm.paymentMethod() == PaymentMethod.GROUP_POINT) {
          pointService.changeGroupPoint(orders.getUser(),
              orders.getTotalPrice() * -1, "포인트 사용 (결제)");
        }

        if (orderPageForm.paymentMethod() == PaymentMethod.GROUP_CARD) {
          orders.getSalePercentage(
              getSalePercentageByGrade(orders.getUser().getGroupUser().getGroupCard().getGrade()));
          payWithGroupCard(orders.getTotalPrice(), orders.getSavedPrice(), orders.getUser());
        }

      }

    } else {
      throw new IllegalArgumentException("Error: 재고 부족으로 인한 상품 주문 불가");
    }
  }

  /**
   * 할인율 주는 로직, 잔당 세일 추가로 먹이려면 시킬 때 카운트 or 종류 2개 이상인거? 이런걸 좀 구하는 로직이 필요할 듯
   *
   * @param grade
   * @return
   */
  private Integer getSalePercentageByGrade(Grade grade) {

    return switch (grade) {
      case BRONZE -> 3;
      case SILVER -> 5;
      case GOLD -> 7;
      case VIP -> 10;
    };
  }

  /**
   * 그룹 없는데 결제수단 그룹카드나 그룹포인트 선택하면 예외 던지는 로직
   *
   * @param username
   */
  private void groupAvailableCheck(String username) {
    if (userRepository.findByUsername(username).getGroupUser() == null) {
      throw new IllegalArgumentException("해당 유저는 그룹이 존재하지 않습니다.");
    }
  }


  /**
   * 그룹카드로 결제할 때 그룹잔고 잔액 차감되는 로직.
   *
   * @param totalPrice 총 결제액
   * @param user       사용한 유저
   */
  private void payWithGroupCard(Integer totalPrice, Integer savedCharge, User user) {
    GroupUser groupUser = groupUserRepository.findByUserUsername(user.getUsername());
    GroupCard groupCard = groupCardRepository.findGroupCardByUserUsername(user.getUsername());

    groupCard.payWithGroupCard(totalPrice, savedCharge);
    groupUser.payWithGroupCard(totalPrice,savedCharge);
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

  private boolean checkCountValidation(List<OrderContent> list) {
    return list.stream()
        .allMatch(s -> s.getProduct().getCount() > s.getCount());
  }

  private void updateProductCount(List<OrderContent> list) {
    list.forEach(s -> s.getProduct().minusCount(s.getCount()));
  }

}

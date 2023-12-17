package com.db.coffeestore9.group.service;

import com.db.coffeestore9.group.domain.GroupCard;
import com.db.coffeestore9.group.repository.GroupCardRepository;
import com.db.coffeestore9.order.common.PaymentMethod;
import com.db.coffeestore9.order.domain.Orders;
import com.db.coffeestore9.order.repository.OrdersRepository;
import com.db.coffeestore9.user.domain.GroupUser;
import com.db.coffeestore9.user.domain.User;
import com.db.coffeestore9.user.repository.GroupUserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupCardService {

  private final GroupUserRepository groupUserRepository;
  private final GroupCardRepository groupCardRepository;
  private final OrdersRepository ordersRepository;

  /**
   * 사용금액 낮은 순서대로 그룹카드 return 해주는 로직
   * @return
   */
  public List<GroupCard> getActiveGroupCardsOrderedByMonthlyUsedChargeAsc() {
    return groupCardRepository.findByActiveTrueOrderByMonthlyUsedChargeAsc();
  }

  /**
   * 사용금액 높은 순서대로 그룹카드 return 해주는 로직
   * @return
   */
  public List<GroupCard> getActiveGroupCardsOrderedByMonthlyUsedChargeDesc() {
    return groupCardRepository.findByActiveTrueOrderByMonthlyUsedCharge();
  }

  /**
   * username으로 그룹카드 찾는 로직
   * @param username
   * @return
   */
  public GroupCard getGroupCard(String username) {
    return groupCardRepository.findGroupCardByUserUsername(username);
  }

  public GroupCard getGroupCard(Long seq) {
    return groupCardRepository.findById(seq).orElse(null);
  }

  /**
   * 다음 등급까지 남은 금액 보여주는 로직
   * @param groupCard
   * @return
   */
  public Integer getConditionForPromotion(GroupCard groupCard) {
    return groupCard.getGrade().getGroupConditionsForPromotion() - groupCard.getMonthlyUsedCharge();
  }

  /**
   * 등급 유지까지 남은 금액 보여주는 로직
   * @param groupCard
   * @return
   */
  public Integer getConditionForDemotion(GroupCard groupCard) {
    return groupCard.getGrade().getGroupConditionsForDemotion() - groupCard.getMonthlyUsedCharge();
  }

  /**
   * 그룹이 활성화 상태인지 + 3명 이상 들어와있는지 확인하는 로직
   *
   * @param groupCard
   * @return
   */
  public boolean checkGroupCardActiveStateAndCreateActive(GroupCard groupCard) {
    return groupCard.isActive() && groupCard.isCreateActive();
  }

  /**
   * 그룹원 찾는 로직
   *
   * @param groupCard 그룹원을 찾으려는 그룹
   * @return 그룹원들(요청 수락한)
   */
  public List<GroupUser> getAcceptedGroupUsers(GroupCard groupCard) {
    return groupUserRepository.findGroupUsersByGroupCardSeq(groupCard.getSeq()).stream().filter(GroupUser::isUserAccepted).toList();
  }

  /**
   * 활성화 요청이 과반수 이상 되었는지 확인하는 로직, 과반수가 넘었을 경우 그룹카드를 활성화 시키면서 그룹원들의 활성화요청상태를 false로 초기화 시킴
   *
   * @param groupCard
   */
  @Transactional
  public void isMajorityActivationRequested(GroupCard groupCard) {
    float totalUsersCount = groupCard.getGroupUsers().size();

    float acceptedUsersCount = groupCard.getGroupUsers().stream()
        .filter(GroupUser::isGroupActiveRequested)
        .count();

    // 과반수 이상 되었을 때
    if (totalUsersCount / 2 <= acceptedUsersCount) {
      groupCard.changeActive(true);

      for (GroupUser groupUser : groupCard.getGroupUsers()) {
        groupUser.changeGroupActiveRequested(true);
      }

    }
  }

  /**
   * 그룹카드로 주문한 내역 찾는 로직
   * @param groupSeq 그룹카드 seq
   * @return List<Orders>
   */
  public List<Orders> findGroupChargeHistory(Long groupSeq) {
    GroupCard groupCard = groupCardRepository.findById(groupSeq).orElseThrow();
    List<Orders> groupOrders = new ArrayList<>();

    groupCard.getGroupUsers().stream().map((GroupUser::getUser)).map(User::getUsername).forEach(
        s -> groupOrders.addAll(
            ordersRepository.findByPaymentMethodAndUserUsername(PaymentMethod.GROUP_CARD, s)));

    return groupOrders;
  }

}

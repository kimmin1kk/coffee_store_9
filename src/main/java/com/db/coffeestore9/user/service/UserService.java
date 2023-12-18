package com.db.coffeestore9.user.service;

import com.db.coffeestore9.group.domain.GroupCard;
import com.db.coffeestore9.group.repository.GroupCardRepository;
import com.db.coffeestore9.security.common.Role;
import com.db.coffeestore9.user.common.RegistrationForm;
import com.db.coffeestore9.user.domain.GroupUser;
import com.db.coffeestore9.user.domain.MonthlyUserData;
import com.db.coffeestore9.user.domain.User;
import com.db.coffeestore9.user.repository.GroupUserRepository;
import com.db.coffeestore9.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;
  private final GroupUserRepository groupUserRepository;
  private final GroupCardRepository groupCardRepository;

  @Transactional
  public void processRegistration(RegistrationForm form) {
    User user = form.toUser();
    user.addAuthority(Role.ROLE_USER);
    userRepository.save(user);
  }

  /**
   * 다음 등급까지 남은 금액 보여주는 로직
   *
   * @param User
   * @return
   */
  public Integer getConditionForPromotion(User user) {
    return user.getGrade().getUserConditionsForPromotion() - user.getMonthlyOrderCharge();
  }

  /**
   * 등급 유지까지 남은 금액 보여주는 로직
   *
   * @param User
   * @return
   */
  public Integer getConditionForDemotion(User user) {
    return user.getGrade().getUserConditionsForDemotion() - user.getMonthlyOrderCharge();
  }

  public User findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  @Transactional
  public void saveMonthlyData(String username) {
    User user = findByUsername(username);
    GroupUser groupUser = groupUserRepository.findByUserUsername(username);

    if (groupUser == null) {
      user.getMonthlyUserData().add(
          MonthlyUserData.builder().user(user).orderAmount(user.getMonthlyOrderCharge())
              .orderCount(user.getMonthlyOrderCount()).build());
      user.resetMonthlyData();

    }
    if (groupUser != null) {
      user.getMonthlyUserData().add(
          MonthlyUserData.builder().user(user).orderAmount(user.getMonthlyOrderCount())
              .orderCount(user.getMonthlyOrderCharge()).rechargeAmount(
                  groupUser.getMonthlyRechargedAmount()).usedAmount(groupUser.getMonthlyUsedAmount())
              .salesAmount(groupUser.getMonthlySalesAmount()).build());
      user.resetMonthlyData();
      groupUser.resetMonthlyData();
    }
    groupCardRepository.findAll().stream().forEach(GroupCard::resetMonthlyData);


  }


}

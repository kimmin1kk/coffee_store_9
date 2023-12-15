package com.db.coffeestore9.group.domain;

import com.db.coffeestore9.global.config.BaseTimeEntity;
import com.db.coffeestore9.user.domain.GroupUser;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RechargeUser extends BaseTimeEntity {

  private Integer rechargeAmount;
  @Builder.Default
  private boolean joined = false;
  @Builder.Default
  private boolean payed = false;

  @Builder.Default
  private boolean penaltyPairAmount = false;

  @ManyToOne
  @JoinColumn(name = "group_user_seq")
  private GroupUser groupUser;

  @ManyToOne
  @JoinColumn(name = "recharge_seq")
  private Recharge recharge;

  public void requestRecharge(Recharge recharge) {
    this.recharge = recharge;
  }

  public void changePayedState(boolean state) {
    this.payed = state;
  }

  public void changeJoinedState(boolean state) {
    this.joined = state;
  }

  public void changePenaltyState(boolean state) {
    this.penaltyPairAmount = state;
  }

  public void getRechargeAmount(Integer amount) {
    this.rechargeAmount = amount;
  }


}

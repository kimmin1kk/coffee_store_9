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
  private boolean joined;
  private boolean payed;

  @ManyToOne()
  @JoinColumn(name = "group_user_seq")
  private GroupUser groupUser;

  @ManyToOne
  @JoinColumn(name = "recharge_seq")
  private Recharge recharge;

}

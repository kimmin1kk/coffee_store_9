package com.db.coffeestore9.user.domain;

import com.db.coffeestore9.global.config.BaseTimeEntity;
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
public class MonthlyUserData extends BaseTimeEntity {

  private Integer orderCount;
  private Integer orderAmount;
  //그룹
  private Integer salesAmount;
  private Integer rechargeAmount;
  private Integer usedAmount;

  @ManyToOne
  @JoinColumn(name = "user_seq")
  private User user;

}
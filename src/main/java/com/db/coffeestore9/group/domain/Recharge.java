package com.db.coffeestore9.group.domain;

import com.db.coffeestore9.global.config.BaseTimeEntity;
import com.db.coffeestore9.global.common.State;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.sql.Timestamp;
import java.util.List;
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
public class Recharge extends BaseTimeEntity {

  private Timestamp finishedDate;
  private Timestamp expirationDate;
  private State state;
  private Integer rechargeAmount;
  private Integer pairAmount;

  @OneToMany(mappedBy = "recharge")
  private List<RechargeUser> rechargeUsers;

}

package com.db.coffeestore9.user.domain;

import com.db.coffeestore9.global.config.BaseTimeEntity;
import com.db.coffeestore9.group.domain.GroupCard;
import com.db.coffeestore9.group.domain.RechargeUser;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.sql.Timestamp;
import java.util.ArrayList;
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
public class GroupUser extends BaseTimeEntity {

  private Integer totalUsedAmount;

  private Integer totalChargedAmount;

  private Integer totalSalesAmount;

  private Integer pairShareAmount;

  private boolean administrator;

  private Timestamp recentlyChargedDate;

  @OneToOne(mappedBy = "groupUser")
  private User user;

  @OneToMany(mappedBy = "groupUser")
  @Builder.Default
  private List<RechargeUser> rechargeUsers = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "group_card_seq")
  private GroupCard groupCard;

}

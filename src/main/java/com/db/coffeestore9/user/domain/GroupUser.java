package com.db.coffeestore9.user.domain;

import com.db.coffeestore9.global.config.BaseTimeEntity;
import com.db.coffeestore9.group.domain.GroupCard;
import com.db.coffeestore9.group.domain.RechargeUser;
import jakarta.persistence.CascadeType;
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
import lombok.ToString;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class GroupUser extends BaseTimeEntity {

  @Builder.Default
  private Integer totalUsedAmount = 0;
  @Builder.Default
  private Integer monthlyUsedAmount = 0;

   @Builder.Default
  private Integer totalChargedAmount = 0;

  @Builder.Default
  private Integer totalSalesAmount = 0;

  @Builder.Default
  private Integer pairShareAmount = 0;

  @Builder.Default
  private boolean administrator = false;

  @Builder.Default
  private boolean userAccepted = false;

  @Builder.Default
  private boolean groupActiveRequested = false;

  @Builder.Default
  private Timestamp recentlyChargedDate = null;

  @OneToOne(mappedBy = "groupUser", cascade = CascadeType.DETACH)
  @ToString.Exclude
  private User user;

  @OneToMany(mappedBy = "groupUser", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  @Builder.Default
  @ToString.Exclude
  private List<RechargeUser> rechargeUsers = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "group_card_seq")
  @ToString.Exclude
  private GroupCard groupCard;

  public void changeUserAccepted(boolean state) {
    this.userAccepted = state;
  }

  public void changePairSharedAmount(Integer amount) {
    this.pairShareAmount = amount;
  }

  public void addPairSharedAmount(Integer amount) {
    this.pairShareAmount += amount;
  }

  public void changeGroupActiveRequested(boolean state) {
    this.groupActiveRequested = state;
  }

  public void payWithGroupCard(Integer amount, Integer savedAmount) {
    this.monthlyUsedAmount += amount;
    this.totalUsedAmount += amount;
    this.totalSalesAmount += savedAmount;
    this.pairShareAmount -= amount;
  }

  public void payWithGroupPoint(Integer savedAmount) {
    this.totalSalesAmount += savedAmount;
  }

}

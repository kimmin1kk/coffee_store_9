package com.db.coffeestore9.group.domain;

import com.db.coffeestore9.global.config.BaseTimeEntity;
import com.db.coffeestore9.global.common.Grade;
import com.db.coffeestore9.rank.domain.TotalRanking;
import com.db.coffeestore9.user.domain.GroupUser;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
public class GroupCard extends BaseTimeEntity {

  @Column(nullable = false)
  private String groupName;
  @Column(nullable = false)
  @Builder.Default
  private Integer charge = 0;
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private Grade grade = Grade.BRONZE;
  @Column(nullable = false)
  @Builder.Default
  private Integer monthlyUsedCharge = 0;
  @Column(nullable = false)
  @Builder.Default
  private Integer totalSalesCharge = 0;
  @Column(nullable = false)
  @Builder.Default
  private Integer point = 0;
  @Column(nullable = false)

  @Builder.Default
  private boolean createActive = false;

  @Builder.Default
  private boolean active = true;

  @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  @JoinColumn(name = "total_ranking_seq")
  @ToString.Exclude
  private TotalRanking totalRanking;

  @OneToMany(mappedBy = "groupCard", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  @ToString.Exclude
  private List<PointUsage> pointUsages;

  @OneToMany(mappedBy = "groupCard", cascade = {CascadeType.PERSIST,
      CascadeType.REMOVE}, orphanRemoval = true)
  @ToString.Exclude
  @Builder.Default
  private List<GroupUser> groupUsers = new ArrayList<>();

  @OneToMany(mappedBy = "groupCard", cascade = {CascadeType.PERSIST,CascadeType.REMOVE}, orphanRemoval = true)
  @ToString.Exclude
  @Builder.Default
  private List<Recharge> recharges = new ArrayList<>();

  public void addGroupUser(GroupUser groupUser) {
    this.groupUsers.add(groupUser);
  }

  public void changeCreateActive(boolean state) {
    this.createActive = state;
  }

  public void changeActive(boolean state) {
    this.active = state;
  }

  public void earnPoint(Integer point) {
    this.point += point;
  }

  public void addCharge(Integer charge) {
    this.charge += charge;
  }

  public void payWithGroupCard(Integer charge, Integer savedCharge) {
    if (this.charge - charge > 0) {
      this.charge -= charge;
      this.monthlyUsedCharge += charge;
      this.totalSalesCharge += savedCharge;
    } else {
      throw new IllegalArgumentException("그룹잔고가 부족합니다!");
    }
  }

  // 포인트가 -로 들어옴
  public void payWithGroupPoint(Integer point) {
    if (this.point + point > 0) {
      this.point += point;
      this.totalSalesCharge -= point;
    }else {
      throw new IllegalArgumentException("포인트가 부족합니다!");
    }
  }

  public void getTotalRanking(TotalRanking totalRanking) {
    this.totalRanking = totalRanking;
  }

}

package com.db.coffeestore9.group.domain;

import com.db.coffeestore9.global.config.BaseTimeEntity;
import com.db.coffeestore9.global.common.Grade;
import com.db.coffeestore9.rank.domain.TotalRanking;
import com.db.coffeestore9.user.domain.GroupUser;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
public class GroupCard extends BaseTimeEntity {

  private String groupName;
  private Integer charge;
  private Grade grade;
  private Integer monthlyUsedCharge;
  private Integer totalSalesCharge;
  private Integer point;
  private boolean active;

  @OneToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "total_ranking_seq")
  private TotalRanking totalRanking;

  @OneToMany(mappedBy = "groupCard", cascade = CascadeType.REMOVE)
  private List<PointUsage> pointUsages;

  @OneToMany(mappedBy = "groupCard", cascade = CascadeType.REMOVE)
  private List<GroupUser> groupUsers;

}

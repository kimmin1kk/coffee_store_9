package com.db.coffeestore9.rank.domain;

import com.db.coffeestore9.global.config.BaseEntity;
import com.db.coffeestore9.group.domain.GroupCard;
import jakarta.persistence.Entity;
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
public class TotalRanking extends BaseEntity {

  private Integer highestRanking;
  private Integer averageRanking;
  private Integer totalEarnedPoint;

  @OneToOne(mappedBy = "totalRanking")
  private GroupCard groupCard;

  @OneToMany(mappedBy = "totalRanking")
  private List<RankInfo> rankingInfos;

}

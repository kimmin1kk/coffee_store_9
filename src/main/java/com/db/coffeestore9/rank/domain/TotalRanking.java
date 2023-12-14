package com.db.coffeestore9.rank.domain;

import com.db.coffeestore9.global.config.BaseEntity;
import com.db.coffeestore9.group.domain.GroupCard;
import jakarta.persistence.CascadeType;
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

  @OneToOne(mappedBy = "totalRanking", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private GroupCard groupCard;

  @OneToMany(mappedBy = "totalRanking", cascade = {CascadeType.PERSIST,
      CascadeType.DETACH}, orphanRemoval = true)
  private List<RankInfo> rankingInfos;

  public void addRankInfos(RankInfo rankInfo) {
    this.rankingInfos.add(rankInfo);
  }

  public void addTotalEarnedPoint(Integer totalEarnedPoint) {
    this.totalEarnedPoint += totalEarnedPoint;
  }

  public void reNewAverageRanking() {
    this.averageRanking =
        rankingInfos.stream().mapToInt(RankInfo::getCurrentRanking).sum() / rankingInfos.size();
  }

  public void reNewHighestRanking() {
    this.highestRanking = rankingInfos.stream().mapToInt(RankInfo::getCurrentRanking).max()
        .getAsInt();
  }

}

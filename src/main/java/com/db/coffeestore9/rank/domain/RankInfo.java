package com.db.coffeestore9.rank.domain;

import com.db.coffeestore9.global.config.BaseTimeEntity;
import com.db.coffeestore9.rank.common.PointRewardTier;
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
public class RankInfo extends BaseTimeEntity {

  @Builder.Default
  private Integer currentRanking = 0;
  @Builder.Default
  private String scheduledPoint = "0";
  @Builder.Default
  private PointRewardTier pointRewardTier = PointRewardTier.NONE;

  @ManyToOne
  @JoinColumn(name = "total_ranking_seq")
  private TotalRanking totalRanking;

  @ManyToOne
  @JoinColumn(name = "ranking_seq")
  private Ranking ranking;

  public void changeCurrentRanking(Integer currentRanking) {
    this.currentRanking = currentRanking;
  }

  public void changePointRewardTier(PointRewardTier pointRewardTier) {
    this.pointRewardTier = pointRewardTier;
  }

  public void changeScheduledPoint(String scheduledPoint) {
    this.scheduledPoint = scheduledPoint;
  }

}

package com.db.coffeestore9.rank.domain;

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
public class RankInfo extends BaseTimeEntity {

  private Integer currentRanking;
  private Integer scheduledPoint;

  @ManyToOne
  @JoinColumn(name = "total_ranking_seq")
  private TotalRanking totalRanking;

  @ManyToOne
  @JoinColumn(name = "ranking_seq")
  private Ranking ranking;

}

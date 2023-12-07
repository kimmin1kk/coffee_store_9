package com.db.coffeestore9.rank.domain;

import com.db.coffeestore9.global.config.BaseTimeEntity;
import com.db.coffeestore9.rank.common.Kind;
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
public class Ranking extends BaseTimeEntity {

  private Timestamp projectedMonth;
  private String eventName;
  private State state;
  private Kind kind;

  @OneToMany(mappedBy = "ranking")
  private List<RankInfo> rankingInfos;

}

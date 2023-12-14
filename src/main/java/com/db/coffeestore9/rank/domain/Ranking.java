package com.db.coffeestore9.rank.domain;

import com.db.coffeestore9.global.config.BaseTimeEntity;
import com.db.coffeestore9.rank.common.Kind;
import com.db.coffeestore9.global.common.State;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

  @Column(nullable = false)
  private Timestamp projectedMonth;
  @Column(nullable = false)
  private String eventName;
  @Builder.Default
  @Enumerated(EnumType.STRING)
  private State state = State.NOT_YET;
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Kind kind;

  @OneToMany(mappedBy = "ranking", cascade = {CascadeType.PERSIST, CascadeType.DETACH})
  private List<RankInfo> rankingInfos;

  public void changeState(State state) {
    this.state = state;
  }

  public void changeKind(Kind kind) {
    this.kind = kind;
  }

}

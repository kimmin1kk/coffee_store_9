package com.db.coffeestore9.group.domain;

import com.db.coffeestore9.global.config.BaseTimeEntity;
import com.db.coffeestore9.global.common.State;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
public class Recharge extends BaseTimeEntity {

  @Builder.Default
  private Timestamp finishedDate = null;

  @Builder.Default
  private Timestamp expirationDate = Timestamp.from(Instant.now().plus(7, ChronoUnit.DAYS));

  @Builder.Default
  @Enumerated(EnumType.STRING)
  private State state = State.ON_PROGRESS;

  @Column(nullable = false)
  private Integer rechargeAmount;

  private Integer pairAmount;

  @OneToMany(mappedBy = "recharge", cascade = {CascadeType.PERSIST,CascadeType.REMOVE}, orphanRemoval = true)
  private List<RechargeUser> rechargeUsers;

  @ManyToOne
  @JoinColumn(name = "group_card_seq")
  private GroupCard groupCard;

  public void changeState(State state) {
    this.state = state;
  }

  public void rechargeFinishedDate() {
    this.finishedDate = new Timestamp(System.currentTimeMillis());
  }

  // pairAmount가 -로 들어올 예정이라서
  public void addPairAmount(Integer pairAmount) {
    this.rechargeAmount += pairAmount;
  }

}

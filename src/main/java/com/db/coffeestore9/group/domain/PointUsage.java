package com.db.coffeestore9.group.domain;

import com.db.coffeestore9.global.config.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.sql.Timestamp;
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
public class PointUsage extends BaseTimeEntity {

  private Timestamp expirationDate;
  @Column(nullable = false)
  private Integer amountPoint;
  @Column(nullable = false)
  private String reasonPoint;

  @ManyToOne
  @JoinColumn(name = "group_card_seq")
  private GroupCard groupCard;


}

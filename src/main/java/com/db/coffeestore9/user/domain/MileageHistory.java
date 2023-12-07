package com.db.coffeestore9.user.domain;

import com.db.coffeestore9.global.config.BaseTimeEntity;
import jakarta.persistence.Entity;
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
public class MileageHistory extends BaseTimeEntity {

  private Integer amountCharge;

  private String reasonCharge;

}

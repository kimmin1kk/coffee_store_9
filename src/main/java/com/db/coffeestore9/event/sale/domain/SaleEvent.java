package com.db.coffeestore9.event.sale.domain;

import com.db.coffeestore9.global.config.BaseTimeEntity;
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
public class SaleEvent extends BaseTimeEntity {

  private String name;
  private Timestamp startDate;
  private Timestamp endDate;
  private Integer salePercent;
  private boolean active;

  @OneToMany(mappedBy = "saleEvent")
  private List<SaleEventContent> saleEventContentList;

}
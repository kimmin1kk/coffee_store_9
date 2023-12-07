package com.db.coffeestore9.event.sale.domain;

import com.db.coffeestore9.global.config.BaseTimeEntity;
import com.db.coffeestore9.product.domain.Product;
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
public class SaleEventContent extends BaseTimeEntity {

  private Integer menuSalePercent;

  @ManyToOne
  @JoinColumn(name = "product_seq")
  private Product product;

  @ManyToOne
  @JoinColumn(name = "sale_event_seq")
  private SaleEvent saleEvent;


}

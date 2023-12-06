package com.db.coffeestore9.order.domain;

import com.db.coffeestore9.global.config.BaseTimeEntity;
import com.db.coffeestore9.product.domain.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderContent extends BaseTimeEntity {

  private Integer count;

  @ManyToOne
  @JoinColumn(name = "order_seq")
  private Orders orders;

  @ManyToOne
  @JoinColumn(name = "product_seq")
  private Product product;

  public OrderContent(Orders orders, Product product, Integer count) {
    this.count = count;
    this.orders = orders;
    this.product = product;
  }

  public void updateCount(int count) {
    this.count = count;
  }

  public void addCount(int count) {
    this.count += count;
  }

}

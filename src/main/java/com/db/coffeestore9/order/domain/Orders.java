package com.db.coffeestore9.order.domain;


import com.db.coffeestore9.global.config.BaseTimeEntity;
import com.db.coffeestore9.order.common.OrderPageForm;
import com.db.coffeestore9.order.common.PaymentMethod;
import com.db.coffeestore9.user.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class Orders extends BaseTimeEntity {


  @Enumerated(EnumType.STRING)
  private PaymentMethod paymentMethod;

  @ManyToOne(cascade = CascadeType.DETACH)
  private User user;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "orders", cascade = CascadeType.REMOVE, orphanRemoval = true)
  @Builder.Default
  private List<OrderContent> orderContentList = new ArrayList<>();

  private Integer totalPrice;

  @Builder.Default
  private Integer salePercentage = 0;

  @Builder.Default
  private Integer savedPrice = 0;

  @Builder.Default
  private boolean instant = false;

  @Builder.Default
  private boolean ordered = false;

  public Orders(User user) {
    this.user = user;
  }


  public void confirmOrder(OrderPageForm pageForm) {
    this.paymentMethod = pageForm.paymentMethod();
    this.totalPrice = pageForm.totalPrice();
    this.ordered = true;
  }

  public void calculateSavedPrice() {
    int price = this.orderContentList.stream()
        .mapToInt(oc -> oc.getProduct().getPrice() * oc.getCount()).sum();
    this.savedPrice = (int) (price * ((double) salePercentage / 100));
    this.totalPrice = price - this.savedPrice;


  }

  public void getSalePercentage(Integer salePercentage) {
    this.salePercentage = salePercentage;
  }
}

package com.db.coffeestore9.order.common;

public record OrderPageForm(boolean orderInstant, PaymentMethod paymentMethod, Integer totalPrice) {

}

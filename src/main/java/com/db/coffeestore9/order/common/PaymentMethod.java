package com.db.coffeestore9.order.common;

public enum PaymentMethod {
    UNKNOWN,
    CASH,
    GROUP_POINT,
    GROUP_CARD;

    public String getDisplayName() {
        return switch (this) {
            case UNKNOWN -> "알 수 없음";
            case CASH -> "현금";
            case GROUP_POINT -> "그룹 포인트";
            case GROUP_CARD -> "그룹 카드";
        };
    }

}

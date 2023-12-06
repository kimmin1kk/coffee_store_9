package com.db.coffeestore9.product.common;

public enum Category {
    ALL,
    COFFEE,
    TEA,
    DRINK,
    BAKERY;

    public String getDisplayName() {
        return switch (this) {
            case ALL -> "전체";
            case COFFEE -> "커피";
            case TEA -> "차";
            case DRINK -> "음료";
            case BAKERY -> "제과";
        };
    }
}

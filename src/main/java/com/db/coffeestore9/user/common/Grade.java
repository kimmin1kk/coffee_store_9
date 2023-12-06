package com.db.coffeestore9.user.common;

public enum Grade {
  BRONZE, SILVER, GOLD, VIP;

  public String getDisplayName() {
    return switch (this) {
      case BRONZE -> "브론즈";
      case SILVER -> "실버";
      case GOLD -> "골드";
      case VIP -> "븨아피";
    };
  }
}

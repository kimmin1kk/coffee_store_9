package com.db.coffeestore9.global.common;

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

  public Integer getGroupConditionsForPromotion() {
    return switch (this) {
      case BRONZE -> 250000;
      case SILVER -> 500000;
      case GOLD -> 1000000;
      case VIP -> 0;
    };
  }

  public Integer getGroupConditionsForDemotion() {
    return switch (this) {
      case BRONZE -> 0;
      case SILVER -> 130000;
      case GOLD -> 250000;
      case VIP -> 500000;
    };
  }

  public Integer getUserConditionsForPromotion() {
    return switch (this) {
      case BRONZE -> 60000;
      case SILVER -> 150000;
      case GOLD -> 300000;
      case VIP -> 0;
    };
  }

  public Integer getUserConditionsForDemotion() {
    return switch (this) {
      case BRONZE -> 0;
      case SILVER -> 30000;
      case GOLD -> 60000;
      case VIP -> 100000;
    };
  }



}

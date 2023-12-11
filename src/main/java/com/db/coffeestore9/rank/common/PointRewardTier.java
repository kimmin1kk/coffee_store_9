package com.db.coffeestore9.rank.common;

public enum PointRewardTier {
  FIRST, SECOND, THIRD, TEN_PERCENT, FORTY_PERCENT, SEVENTY_PERCENT, NONE;

  public String getDisplayName() {
    return switch (this) {
      case FIRST -> "1등";
      case SECOND -> "2등";
      case THIRD -> "3등";
      case TEN_PERCENT -> "상위 10%";
      case FORTY_PERCENT -> "상위 40%";
      case SEVENTY_PERCENT -> "상위 70%";
      case NONE -> "해당 사항 없음";
    };
  }
}

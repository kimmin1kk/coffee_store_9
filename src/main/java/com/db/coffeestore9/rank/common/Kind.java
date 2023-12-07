package com.db.coffeestore9.rank.common;

public enum Kind {
  MAX_PAY_RANK,WORST_ITEM_RANK, BEST_ITEM_RANK;

  public String getDisplayName() {
    return switch (this) {
      case MAX_PAY_RANK -> "돈 많이 낸 그룹";
      case BEST_ITEM_RANK ->"제일 많이 팔리는 상품 많이 산 그룹";
      case WORST_ITEM_RANK -> "제일 안 팔리는 상품 많이 산 그룹";
    };


  }
}

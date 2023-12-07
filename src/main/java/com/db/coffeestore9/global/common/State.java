package com.db.coffeestore9.global.common;

public enum State {

  NOT_YET, ON_PROGRESS, FINISHED, EXPIRED;

  public String getDisplayName() {
    return switch (this) {
      case NOT_YET -> "진행전";
      case ON_PROGRESS -> "진행중";
      case FINISHED -> "종료됨";
      case EXPIRED -> "만료됨";
    };
  }
}

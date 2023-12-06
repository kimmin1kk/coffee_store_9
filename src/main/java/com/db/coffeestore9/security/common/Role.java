package com.db.coffeestore9.security.common;

public enum Role {
  ROLE_ADMIN,
  ROLE_USER;

  public String getDisplayName() {
    return switch (this) {
      case ROLE_ADMIN -> "어드민";
      case ROLE_USER -> "유저";
    };
  }
}

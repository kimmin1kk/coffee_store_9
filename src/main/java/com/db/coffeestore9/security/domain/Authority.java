package com.db.coffeestore9.security.domain;

import com.db.coffeestore9.global.config.BaseTimeEntity;
import com.db.coffeestore9.security.common.Role;
import com.db.coffeestore9.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Authority extends BaseTimeEntity {

  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_seq", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  private User user;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;
}

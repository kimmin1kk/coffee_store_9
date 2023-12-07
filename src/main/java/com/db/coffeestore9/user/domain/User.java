package com.db.coffeestore9.user.domain;

import com.db.coffeestore9.global.config.BaseTimeEntity;
import com.db.coffeestore9.order.domain.Orders;
import com.db.coffeestore9.security.common.Role;
import com.db.coffeestore9.security.domain.Authority;
import com.db.coffeestore9.global.common.Grade;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

  @Column(nullable = false, unique = true)
  private String username;
  @Column(nullable = false)
  private String password;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private Grade grade;
  @Column(nullable = false)
  private Integer mileage;
  @Column(nullable = false)
  private Timestamp recentActiveDate;
  @Column(nullable = false)
  private Integer monthlyOrderCount;
  @Column(nullable = false)
  private Integer monthlyOrderCharge;

  @Column(columnDefinition = "boolean default true")
  @Builder.Default
  private boolean enabled = true;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.REMOVE)
  @Builder.Default
  private Set<Authority> authorities = new HashSet<>();

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
  @Builder.Default
  private List<Orders> ordersList = new ArrayList<>();

  @OneToMany(mappedBy = "user")
  private List<MonthlyUserData> monthlyUserData;

  @OneToOne
  @JoinColumn(name = "group_user_seq")
  private GroupUser groupUser;


  public void addAuthority(Role role) {
    if (!this.authorities.isEmpty() && (authorities.stream()
        .anyMatch(authority -> authority.getRole().equals(role)))) {
      throw new IllegalArgumentException("해당 유저는 이미 해당 권한을 가지고 있습니다.");
    }
    Authority authority = Authority.builder().role(role).user(this).build();
    this.authorities.add(authority);
  }

}

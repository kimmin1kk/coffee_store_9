package com.db.coffeestore9.global.config;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.util.Objects;
import lombok.Getter;
import org.hibernate.proxy.HibernateProxy;

@MappedSuperclass
@Getter
public class BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long seq;

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    if (!(o instanceof BaseEntity that)) {
      return false;
    }
    Class<?> oEffectiveClass = o instanceof HibernateProxy hibernateProxy
        ? hibernateProxy.getHibernateLazyInitializer()
        .getPersistentClass() : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy hibernateProxy
        ? hibernateProxy.getHibernateLazyInitializer()
        .getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) {
      return false;
    }
    return getSeq() != null && Objects.equals(getSeq(), that.getSeq());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy hibernateProxy
        ? hibernateProxy.getHibernateLazyInitializer()
        .getPersistentClass().hashCode() : getClass().hashCode();
  }
}

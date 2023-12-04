package com.db.coffeestore9.security.repository;

import com.db.coffeestore9.security.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}

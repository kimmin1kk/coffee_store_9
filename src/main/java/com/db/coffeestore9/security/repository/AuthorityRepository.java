package com.db.coffeestore9.security.repository;

import com.db.coffeestore9.security.domain.Authority;
import org.springframework.data.repository.CrudRepository;

public interface AuthorityRepository extends CrudRepository<Authority, Long> {
}

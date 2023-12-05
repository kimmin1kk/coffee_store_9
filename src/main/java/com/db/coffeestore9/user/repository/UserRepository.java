package com.db.coffeestore9.user.repository;

import com.db.coffeestore9.user.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

}

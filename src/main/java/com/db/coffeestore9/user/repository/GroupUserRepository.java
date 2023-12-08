package com.db.coffeestore9.user.repository;

import com.db.coffeestore9.user.domain.GroupUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupUserRepository extends JpaRepository<GroupUser, Long> {

}
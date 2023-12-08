package com.db.coffeestore9.user.repository;

import com.db.coffeestore9.user.domain.MonthlyUserData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonthlyUserDataRepository extends JpaRepository<MonthlyUserData, Long> {

}
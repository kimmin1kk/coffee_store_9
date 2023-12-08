package com.db.coffeestore9.user.repository;

import com.db.coffeestore9.user.domain.MileageHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MileageHistoryRepository extends JpaRepository<MileageHistory, Long> {

}
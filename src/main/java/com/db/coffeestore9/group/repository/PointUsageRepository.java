package com.db.coffeestore9.group.repository;

import com.db.coffeestore9.group.domain.GroupCard;
import com.db.coffeestore9.group.domain.PointUsage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointUsageRepository extends JpaRepository<PointUsage, Long> {

  List<PointUsage> findPointUsagesByGroupCard(GroupCard groupCard);
}
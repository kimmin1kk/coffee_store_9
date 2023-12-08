package com.db.coffeestore9.rank.repository;

import com.db.coffeestore9.rank.domain.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankingRepository extends JpaRepository<Ranking, Long> {

}
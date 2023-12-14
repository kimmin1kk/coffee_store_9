package com.db.coffeestore9.rank.repository;

import com.db.coffeestore9.rank.domain.TotalRanking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TotalRankingRepository extends JpaRepository<TotalRanking, Long> {

  TotalRanking findTotalRankingByGroupCardSeq(Long seq);

}
package com.db.coffeestore9.rank.repository;

import com.db.coffeestore9.rank.domain.RankInfo;
import com.db.coffeestore9.rank.domain.Ranking;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankInfoRepository extends JpaRepository<RankInfo, Long> {

  List<RankInfo> findRankInfosByRankingSeq(Long seq);

  List<RankInfo> findTop3ByRankingOrderByCurrentRankingAsc(Ranking ranking);
}
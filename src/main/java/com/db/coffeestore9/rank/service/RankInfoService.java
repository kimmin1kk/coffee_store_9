package com.db.coffeestore9.rank.service;

import com.db.coffeestore9.rank.domain.RankInfo;
import com.db.coffeestore9.rank.domain.Ranking;
import com.db.coffeestore9.rank.repository.RankInfoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RankInfoService {

  private final RankInfoRepository rankInfoRepository;

  public List<RankInfo> getRankingTop3(Ranking ranking) {
    return rankInfoRepository.findTop3ByRankingOrderByCurrentRankingAsc(ranking);

  }

}

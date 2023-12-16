package com.db.coffeestore9.rank.service;

import com.db.coffeestore9.group.repository.GroupCardRepository;
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
  private final GroupCardRepository groupCardRepository;

  /**
   * 현재 진행중인 랭킹 이벤트의 top3를 볼 수 있는 로직
   *
   * @param ranking
   * @return
   */
  public List<RankInfo> getRankingTop3(Ranking ranking) {
    return rankInfoRepository.findTop3ByRankingOrderByCurrentRankingAsc(ranking);

  }

  /**
   * 내 그룹의 현재 진행중인 랭킹에서의 등수,예상 포인트 등을 확인할 수 있는 로직
   *
   * @param ranking
   * @param username
   * @return
   */
  public RankInfo getMyRankInfo(Ranking ranking, String username) {
    return rankInfoRepository.findByRankingAndTotalRankingGroupCard(ranking,
        groupCardRepository.findGroupCardByUserUsername(username));

  }

}

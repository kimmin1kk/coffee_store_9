package com.db.coffeestore9.rank.service;

import com.db.coffeestore9.group.domain.GroupCard;
import com.db.coffeestore9.rank.domain.TotalRanking;
import com.db.coffeestore9.rank.repository.TotalRankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TotalRankingService {

  private final TotalRankingRepository totalRankingRepository;


  /**
   * 그룹카드의 토탈랭킹을 리턴하는 로직
   *
   * @param groupCard 그룹카드, 아마 groupCardService.getMyGroup(principal.getName)로 땡겨와서 쓸 예정
   * @return GroupCard - TotalRanking
   */
  public TotalRanking getGroupTotalRanking(GroupCard groupCard) {
    return totalRankingRepository.findTotalRankingByGroupCardSeq(groupCard.getSeq());
  }

}

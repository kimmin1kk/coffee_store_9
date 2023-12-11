package com.db.coffeestore9.group.service;

import com.db.coffeestore9.group.domain.GroupCard;
import com.db.coffeestore9.group.domain.PointUsage;
import com.db.coffeestore9.group.repository.GroupCardRepository;
import com.db.coffeestore9.group.repository.PointUsageRepository;
import com.db.coffeestore9.rank.common.PointRewardTier;
import java.sql.Timestamp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointService {

  private final PointUsageRepository pointUsageRepository;
  private final GroupCardRepository groupCardRepository;

  /**
   * 포인트 지급,차감 로직
   *
   * @param groupSeq GroupCard seq
   * @param point    +-Point
   * @param message  Reason
   */
  @Transactional
  public void changeGroupPoint(Long groupSeq, Integer point, String message) {

    GroupCard groupCard = groupCardRepository.findById(groupSeq).orElseThrow();

    groupCard.changePoint(point);

    groupCard.getPointUsages().add(pointUsageRepository.save(
        PointUsage.builder().groupCard(groupCard)
            .amountPoint(point).reasonPoint(message).expirationDate(new Timestamp(System.currentTimeMillis() + 96000)).build()));

  }

  /**
   * 랭킹 별 포인트 얼마 받을지 정해서 리턴해 주는 로직
   *
   * @param groupSeq 1,2,3등 보상이 월간사용금액에서 퍼센트로 지급해주는거라서 groupCard 정보 필요, 인자로 받아야 함
   * @param rewardTier 그룹의 티어
   * @return 산정된 포인트 리턴 이걸 changeGroupPoint 메서드의 point 인자에 넣으면 됨
   */
  public Integer awardPointByRanking(Long groupSeq, PointRewardTier rewardTier) {
    GroupCard groupCard = groupCardRepository.findById(groupSeq).orElseThrow();
    Integer monthlyUsedAmount = groupCard.getMonthlyUsedCharge();

    return switch (rewardTier) {
      case FIRST -> (int) (monthlyUsedAmount * 0.4);
      case SECOND -> (int) (monthlyUsedAmount * 0.2);
      case THIRD -> (int) (monthlyUsedAmount * 0.1);
      case TEN_PERCENT -> 10000;
      case FORTY_PERCENT -> 5000;
      case SEVENTY_PERCENT -> 4000;
      case NONE -> 0;
    };
  }


}

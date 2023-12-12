package com.db.coffeestore9.rank.service;

import com.db.coffeestore9.global.common.State;
import com.db.coffeestore9.group.domain.GroupCard;
import com.db.coffeestore9.rank.common.Kind;
import com.db.coffeestore9.rank.common.PointRewardTier;
import com.db.coffeestore9.rank.domain.RankInfo;
import com.db.coffeestore9.rank.domain.Ranking;
import com.db.coffeestore9.rank.domain.TotalRanking;
import com.db.coffeestore9.rank.repository.RankInfoRepository;
import com.db.coffeestore9.rank.repository.RankingRepository;
import com.db.coffeestore9.rank.repository.TotalRankingRepository;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RankingService {

  private final RankingRepository rankingRepository;
  private final RankInfoRepository rankInfoRepository;
  private final TotalRankingRepository totalRankingRepository;

  public Ranking getRanking(Long rankingSeq) {
    return rankingRepository.findById(rankingSeq).orElseThrow();
  }

  /**
   * 랭킹 스케쥴 등록하는 로직, 그 외에도 측정월에 따라 상태를 바꿔주는 로직, 랭킹 집계를 하는 로직 등등이 더 필요한 상태
   *
   * @param yymm
   * @param eventKind
   * @param eventName
   */
  @Transactional
  public void createRankingSchedule(Integer yymm, Kind eventKind, String eventName) {
    Ranking ranking = Ranking.builder().projectedMonth(convertIntegerToTimestamp(yymm))
        .eventName(eventName)
        .kind(eventKind).build();

    rankingRepository.save(ranking);

  }

  /**
   * 랭킹 이벤트를 시작하는 로직, 내부적으로 checkRankingStart 메서드를 호출해 시작할 시기가 되었는지 확인하고 그 상태에 따라 랭킹의 상태를 바꿔주고
   * GroupCard -> TotalRanking 생성 -> RankInfo 생성 및 서로 연결 연결.. 하면 끝
   *
   * @param rankingSeq Ranking seq
   */
  @Transactional
  public void startRankingSchedule(Long rankingSeq) {
    Ranking ranking = getRanking(rankingSeq);
    if (checkRankingStart(rankingSeq)) {

      totalRankingRepository.findAll().stream().filter(s -> s.getGroupCard().isActive())
          .forEach(s -> s.getRankingInfos().add(RankInfo.builder().totalRanking(s).ranking(ranking).build()
              ));

      ranking.changeState(State.ON_PROGRESS);
    } else {
      throw new IllegalArgumentException("해당 랭킹 이벤트는 아직 시작할 시기가 되지 않았습니다. ?");
    }
  }


  /**
   * 랭킹 seq, 이벤트를 시작 해야 하는지 현재 시간이랑 비교해서 판단 하는 로직
   *
   * @param rankingSeq Ranking seq
   * @return true or false
   */
  private boolean checkRankingStart(Long rankingSeq) {
    Ranking ranking = getRanking(rankingSeq);
    return ranking.getProjectedMonth().before(new Timestamp(System.currentTimeMillis()));
  }

  /**
   * 랭킹 이벤트를 변경하는 로직, 이미 시작된 상태라면 수정할 수 없다.
   *
   * @param rankingSeq
   * @param kind
   */
  @Transactional
  public void changeRankingKind(Long rankingSeq, Kind kind) {
    Ranking ranking = getRanking(rankingSeq);

    if (ranking.getState() == State.NOT_YET) {
      ranking.changeKind(kind);
    } else {
      throw new IllegalArgumentException("해당월 랭킹 이벤트가 진행전 상태일때만 수정할 수 있습니다.");
    }

  }

  /**
   * 랭킹 구하는 로직, 활성화된 GroupCard -> monthlyUsedCharge로 내림차순 정렬하고 그 리스트 순서별로 RankInfo ->
   * currentRanking(순위) 맥이기
   */
  @Transactional
  public void getGroupsRanking(Long rankingSeq) {
    Ranking ranking = getRanking(rankingSeq);
    List<RankInfo> rankInfos = rankInfoRepository.findRankInfosByRankingSeq(rankingSeq);

    List<GroupCard> sortedGroupCard = ranking.getRankingInfos().stream()
        .map(RankInfo::getTotalRanking)
        .map(TotalRanking::getGroupCard).sorted(Comparator.comparing(
            GroupCard::getMonthlyUsedCharge).reversed()).toList();

    for (RankInfo rankInfo : rankInfos) {
      GroupCard groupCard = rankInfo.getTotalRanking().getGroupCard();
      int index = sortedGroupCard.indexOf(groupCard);
      rankInfo.changeCurrentRanking(index + 1);
    }

    getGroupRewardTier(rankInfos.size(), rankInfos);
    getScheduledPoint(rankInfos);


  }

  /**
   * 등수 별로 GroupInfo에 등급을 지정해주는 로직, getGroupsRanking()에서 호출됨
   *
   * @param joinedGroups 이번 랭킹 이벤트에 참여한 그룹 수
   * @param rankInfos    참어한 그룹들(RankInfo)
   */
  private void getGroupRewardTier(Integer joinedGroups, List<RankInfo> rankInfos) {
    int top10Percent = (int) Math.ceil(joinedGroups * 0.1);
    int top40Percent = (int) Math.ceil(joinedGroups * 0.4);
    int top70Percent = (int) Math.ceil(joinedGroups * 0.7);

    for (RankInfo rankInfo : rankInfos) {
      int currentRanking = rankInfo.getCurrentRanking();

      if (currentRanking == 1) {
        rankInfo.changePointRewardTier(PointRewardTier.FIRST);
      } else if (currentRanking == 2) {
        rankInfo.changePointRewardTier(PointRewardTier.SECOND);
      } else if (currentRanking == 3) {
        rankInfo.changePointRewardTier(PointRewardTier.THIRD);
      } else if (currentRanking >= top10Percent) {
        rankInfo.changePointRewardTier(PointRewardTier.TEN_PERCENT);
      } else if (currentRanking >= top40Percent) {
        rankInfo.changePointRewardTier(PointRewardTier.FORTY_PERCENT);
      } else if (currentRanking >= top70Percent) {
        rankInfo.changePointRewardTier(PointRewardTier.SEVENTY_PERCENT);
      } else {
        rankInfo.changePointRewardTier(PointRewardTier.NONE);
      }

    }
  }

  /**
   * 수령예정포인트 구하는 로직. 그룹 랭킹 정보 보려고 할 때 호출 하면 될 듯, getGroupsRanking()에서 호출됨, 이건 예정 금액이기 때문에 1,2,3등 한테
   * 굳이 그룹카드 정보가져와서 수치 보여줄 필요 없을거라 판단해서 String 으로 반환
   *
   * @param rankInfos 이번 랭킹에 참가하는 그룹랭킹정보. 랭킹 Seq로 바꿔도 괜찮을 듯 이거는 상의해서 정하면 되는 내용
   */

  private void getScheduledPoint(List<RankInfo> rankInfos) {
    for (RankInfo rankInfo : rankInfos) {
      if (rankInfo.getPointRewardTier() == PointRewardTier.FIRST) {
        rankInfo.changeScheduledPoint("월간 사용금액의 40%");
      } else if (rankInfo.getPointRewardTier() == PointRewardTier.SECOND) {
        rankInfo.changeScheduledPoint("월간 사용금액의 20%");
      } else if (rankInfo.getPointRewardTier() == PointRewardTier.THIRD) {
        rankInfo.changeScheduledPoint("월간 사용금액의 10%");
      } else if (rankInfo.getPointRewardTier() == PointRewardTier.TEN_PERCENT) {
        rankInfo.changeScheduledPoint("10000");
      } else if (rankInfo.getPointRewardTier() == PointRewardTier.FORTY_PERCENT) {
        rankInfo.changeScheduledPoint("7000");
      } else if (rankInfo.getPointRewardTier() == PointRewardTier.SEVENTY_PERCENT) {
        rankInfo.changeScheduledPoint("4000");
      }
    }


  }

  /**
   * Integer yymm 넣으면 Timestamp 형식으로 바꿔주는 로직
   *
   * @param yymm 년월 ex) 2311
   * @return Timestamp ex) 2023-11-02로 바꿔서 반환
   */
  private Timestamp convertIntegerToTimestamp(Integer yymm) {
    int year = yymm / 100 + 2000;
    int month = yymm % 100;
    LocalDate date = LocalDate.of(year, month, 2);

    return Timestamp.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
  }


}


package com.db.coffeestore9.rank.service;

import com.db.coffeestore9.global.common.State;
import com.db.coffeestore9.group.domain.GroupCard;
import com.db.coffeestore9.group.service.PointService;
import com.db.coffeestore9.rank.common.CreateRankingForm;
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
import java.time.LocalDateTime;
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
  private final PointService pointService;

  /**
   * 랭킹 엔티티 찾는 로직
   *
   * @param rankingSeq
   * @return
   */
  public Ranking getRanking(Long rankingSeq) {
    return rankingRepository.findById(rankingSeq).orElseThrow();
  }

  /**
   * 종료된 랭킹 등 지금까지의 모든 랭킹들을 보는 로직
   *
   * @return
   */
  public List<Ranking> getAllRankings() {
    return rankingRepository.findAll();
  }

  /**
   * 종료된 랭킹만 보는 로직
   * @return
   */
  public List<Ranking> getFinishedRankings() {

    return rankingRepository.findAll().stream().filter(s -> s.getState() == State.FINISHED)
        .toList();
  }

  /**
   * 시작되지 않은 랭킹만 보는 로직
   * @return
   */
  public List<Ranking> getNotYetRankings() {

    return rankingRepository.findAll().stream().filter(s -> s.getState() == State.NOT_YET).toList();
  }


  /**
   * 현재 진행중인 랭킹 이벤트를 보는 로직
   * 한번에 하나밖에 진행 못하니까 List로 받지않음 그냥 있거나 없거나
   * @return
   */
  public Ranking getActiveRanking() {
    return rankingRepository.findAll().stream().filter(s -> s.getState() == State.ON_PROGRESS)
        .findFirst().orElse(null);
  }


  /**
   * 랭킹 스케쥴 등록하는 로직, 그 외에도 측정월에 따라 상태를 바꿔주는 로직, 랭킹 집계를 하는 로직 등등이 더 필요한 상태
   *
   * @param createRankingForm
   */
  @Transactional
  public void createRankingSchedule(CreateRankingForm createRankingForm) {
    Ranking ranking = Ranking.builder().projectedMonth(convertIntegerToTimestamp(
            createRankingForm.yymm()))
        .eventName(createRankingForm.eventName())
        .kind(createRankingForm.eventKind()).build();

    rankingRepository.save(ranking);

  }

  /**
   * 랭킹 이벤트를 시작하는 로직, 내부적으로 checkRankingStart 메서드를 호출해 시작할 시기가 되었는지 확인하고 그 상태에 따라 랭킹의 상태를 바꿔주고
   * GroupCard -> TotalRanking 생성 -> RankInfo 생성 및 서로 연결 연결.. 하면 끝
   * <p>
   * -> 시연을 위해 yymm (년월) 인자값 추가로 받아서 시작예정년월과 일치하면 랭킹 이벤트 시작
   *
   * @param rankingSeq Ranking seq
   */
  @Transactional
  public void startRankingSchedule(Long rankingSeq, Timestamp yymm) {
    if (getActiveRanking() == null) { //현재 진행중인 랭킹이 없어야함
      Ranking ranking = getRanking(rankingSeq);
      if (checkRankingStart(rankingSeq, convertTimestampToInteger(yymm))) { //rankingSeq와 yymm으로 시작할 기간이 된 랭킹인지 확인함

        totalRankingRepository.findAll().stream().filter(s -> s.getGroupCard().isActive())
            .forEach(s -> s.getRankingInfos()
                .add(RankInfo.builder().totalRanking(s).ranking(ranking).build()
                ));

        ranking.changeState(State.ON_PROGRESS);
      } else {
        throw new IllegalArgumentException("해당 랭킹 이벤트는 아직 시작할 시기가 되지 않았습니다. ?");
      }
    } else {
      throw new IllegalArgumentException("현재 진행중인 랭킹 이벤트가 존재합니다.");
    }
  }


  /**
   * 랭킹 seq, 이벤트를 시작 해야 하는지 현재 시간이랑 비교해서 판단 하는 로직 -> 시연을 위해서 인자로 yymm(년월)넣어서 Timestamp 타입으로 변환한 후
   * 같으면 true
   *
   * @param rankingSeq Ranking seq
   * @param yymm       시작하려는 년월
   * @return true or false
   */
  private boolean checkRankingStart(Long rankingSeq, Integer yymm) {
    Ranking ranking = getRanking(rankingSeq);
    return ranking.getProjectedMonth().equals(convertIntegerToTimestamp(yymm));
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
   * ScheduledTasks에서 호출되는 메서드. 그룹 랭킹 갱신해주는 로직, 활성화된 GroupCard -> monthlyUsedCharge로 내림차순 정렬하고 그 리스트
   * 순서별로 RankInfo -> currentRanking(순위) 맥이기 위에 주석친 메서드와 거의 동일하나 이 메서드는 Schedule 걸어두고 쓸 예정
   */
  @Transactional
  public void renewGroupsRanking() {
    Ranking ranking = getActiveRanking();
    List<RankInfo> rankInfos = rankInfoRepository.findRankInfosByRankingSeq(ranking.getSeq());

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
   * 랭킹 이벤트 종료 로직 Ranking -> State 변경 GroupCard -> PointUsages.add / RankInfos.add / point ++
   * TotalRank -> total_earned_point ++ / 최고 등수 (갱신하면 업데이트) / 평균등수(지금까지 등수 다 더해서 1/n)
   *
   * @param rankingSeq
   */
  @Transactional
  public void processRankingEnds(Long rankingSeq) {
    Ranking ranking = rankingRepository.findById(rankingSeq).orElseThrow();
    ranking.changeState(State.FINISHED);

    ranking.getRankingInfos().forEach(s -> {
      s.getTotalRanking().addRankInfos(s);
      //포인트 지급하는 부분,,
      pointService.changeGroupPoint(
          s.getTotalRanking().getGroupCard().getGroupUsers().get(0).getUser(),
          pointService.awardPointByRanking(s.getTotalRanking().getGroupCard().getSeq(),
              s.getPointRewardTier()), "등수 보상");
      // TotalRank -> 누적 포인트, 평균 등수, 최고 등수
      s.getTotalRanking().addTotalEarnedPoint(
          pointService.awardPointByRanking(s.getTotalRanking().getGroupCard().getSeq(),
              s.getPointRewardTier()));
      s.getTotalRanking().reNewAverageRanking();
      s.getTotalRanking().reNewHighestRanking();
      //
    });
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

  /**
   * Timestamp를 받아서 년월 Integer로 바꿔주는 로직
   *
   * @param timestamp Timestamp ex) 2023-11-02
   * @return Integer ex) 2311로 바꿔서 반환
   */
  private Integer convertTimestampToInteger(Timestamp timestamp) {
    LocalDateTime dateTime = timestamp.toLocalDateTime();
    int year = dateTime.getYear() - 2000;
    int month = dateTime.getMonthValue();
    return year * 100 + month;
  }


}


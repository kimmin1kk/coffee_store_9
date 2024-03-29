package com.db.coffeestore9.group.service;


import com.db.coffeestore9.global.common.State;
import com.db.coffeestore9.group.common.RequestPairAmountPenalty;
import com.db.coffeestore9.group.common.RequestRechargeForm;
import com.db.coffeestore9.group.domain.GroupCard;
import com.db.coffeestore9.group.domain.Recharge;
import com.db.coffeestore9.group.domain.RechargeUser;
import com.db.coffeestore9.group.repository.GroupCardRepository;
import com.db.coffeestore9.group.repository.RechargeRepository;
import com.db.coffeestore9.group.repository.RechargeUserRepository;
import com.db.coffeestore9.user.domain.GroupUser;
import com.db.coffeestore9.user.repository.GroupUserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RechargeService {

  private final RechargeRepository rechargeRepository;
  private final RechargeUserRepository rechargeUserRepository;
  private final GroupUserRepository groupUserRepository;
  private final GroupCardRepository groupCardRepository;

  /**
   * 진행중인 충전요청과 이미 끝난 충전요청을 List 형태로 반환하는 로직
   *
   * @param groupCard
   * @return
   */
  public List<Recharge> getRechargeHistory(GroupCard groupCard) {
    return groupCard.getRecharges().stream()
        .filter(s -> s.getState() == State.FINISHED || s.getState() == State.ON_PROGRESS).toList();
  }

  public Recharge getOnProgressRecharge(List<Recharge> recharges) {
    return recharges.stream().filter(s -> s.getState() == State.ON_PROGRESS).findFirst()
        .orElseThrow();
  }

  /**
   * Usernames과 Recharge의 seq를 받아 유저별 충전 항목 (RechargeUser) 을 받아오는 로직
   *
   * @param usernames usernames
   * @param seq       Recharge Seq
   * @return
   */
  private List<RechargeUser> convertUsernamesAndRechargeSeqToRechargeUsers(List<String> usernames,
      Long seq) {
    return usernames.stream()
        .map(
            s -> rechargeUserRepository.findByGroupUserUserUsernameAndRechargeSeq(s, seq))
        .toList();
  }

  /**
   * Usernames과 Recharge의 seq를 받아 유저별 충전 항목 (RechargeUser) 을 받아오는 로직
   *
   * @param usernames usernames
   * @param seq       GroupCard seq
   * @return
   */
  private List<RechargeUser> convertUsernamesAndGroupSeqToRechargeUsers(List<String> usernames,
      Long seq) {
    return usernames.stream()
        .map(
            //한번 충전한 뒤로 두번째 충전할 때 결과가 2개라 오류뜸
            //유저네임과 그룹카드seq만으로는 rechargeUser 2개가 반환되기 때문에 오류가 뜬건데..
            s -> rechargeUserRepository.findTopByGroupUserUserUsernameAndGroupUserGroupCardSeqOrderByCreatedDateDesc(
                s, seq))
        .toList();
  }


  /**
   * Username과 Recharge의 seq를 받아 유저별 충전 내역을 받아오는 로직
   *
   * @param username
   * @param seq
   * @return
   */
  private RechargeUser convertUsernameAndSeqToRechargeUser(String username, Long seq) {
    return rechargeUserRepository.findByGroupUserUserUsernameAndRechargeSeq(username,
        seq);
  }

  /**
   * 충전에 참여하는 유저를 List 형태로 받아오는 로직
   *
   * @param users
   * @return JoinedRechargeUsers
   */
  public List<RechargeUser> getJoinedUsers(List<RechargeUser> users) {
    return users.stream().filter(RechargeUser::isJoined).toList();
  }


  /**
   * 이미 돈 낸 유저를 List 형태로 받아오는 로직
   *
   * @param users
   * @return
   */
  private List<RechargeUser> getPayedUsers(List<RechargeUser> users) {
    return users.stream().filter(RechargeUser::isPayed).toList();
  }

  /**
   * 양심금을 지불해야하는 유저를 List 형태로 받아오는 로직
   *
   * @param users
   * @return
   */
  private List<RechargeUser> getPenaltyUsers(List<RechargeUser> users) {
    return users.stream().filter(RechargeUser::isPenaltyPairAmount).toList();
  }

  /**
   * Recharge Seq를 통해 Recharge를 가져오는 로직
   *
   * @param seq
   * @return
   */
  public Recharge getRecharge(Long seq) {
    return rechargeRepository.findById(seq).orElseThrow();
  }

  /**
   * 양심금 오바된 사람이 있는지 확인하는 로직
   *
   * @return 오바된 사람이 있으면 True
   */
  public boolean checkUsersPairAmount(Recharge recharge) {
    List<RechargeUser> rechargeUsers = recharge.getRechargeUsers().stream()
        .filter(RechargeUser::isJoined).toList();

    return rechargeUsers.stream().anyMatch(s -> s.getGroupUser().getPairShareAmount() < 0);
  }

//----------------------------------------------------------------------------------------------------

  /**
   * 충전 인원 및 충전 금액 정하고 충전 요청 할 때의 로직 충전 요청 하는 로직 Recharge recharge =
   * requestRecharge(requestRechargeForm); processRecharge(recharge); 컨트롤러에선 이런 식으로 쓸 예정
   *
   * @param requestRechargeForm
   * @return rechargeAmount, pairAmount, rechargeUsers가 있는 Recharge 리턴
   */
  @Transactional
  public Recharge requestRecharge(RequestRechargeForm requestRechargeForm) {
    // 들어온 그룹원 전체 -> createRechargeUsers()
    List<RechargeUser> rechargeUsers = createRechargeUsers(requestRechargeForm.groupSeq()).stream()
        .toList();
    // 충전 요청에 선택된 그룹원 -> joinRecharge()
    joinRecharge(
        convertUsernamesAndGroupSeqToRechargeUsers(requestRechargeForm.usernames(),
            requestRechargeForm.groupSeq()
        ));

    GroupCard groupCard = groupCardRepository.findById(requestRechargeForm.groupSeq())
        .orElseThrow();
    Recharge recharge = Recharge.builder()
        .rechargeAmount(requestRechargeForm.amount())
        .groupCard(groupCard)
        .pairAmount(getPairAmount(requestRechargeForm.amount(), rechargeUsers))
        .rechargeUsers(rechargeUsers)
        .build();
    groupCard.getRecharges().add(recharge);
    rechargeUsers.forEach(s -> s.requestRecharge(recharge));
    getRechargeAmount(recharge.getRechargeUsers(), recharge.getRechargeAmount());
    return recharge;
  }

  /**
   * 그룹원 별 충전 항목 만드는 로직, 그룹원 전체를 넣는 이유는 양심금을 계산하기 위해서
   *
   * @param groupSeq 그룹원 전체를 넣기 위해 그룹카드의 주식별자가 필요함
   * @return List<RechargeUser>  가입하지 않은 유저 필터링된 상태
   */
  private List<RechargeUser> createRechargeUsers(Long groupSeq) {
    List<GroupUser> groupUsers = groupUserRepository.findGroupUsersByGroupCardSeq(groupSeq);
    return groupUsers.stream().filter(GroupUser::isUserAccepted).map(s -> {
      RechargeUser rechargeUser = RechargeUser.builder().groupUser(s).build();
      s.getRechargeUsers().add(rechargeUser);
      return rechargeUser;
    }).toList();
  }


  /**
   * 공정분배금액 구하는 로직, 이건 그룹원의 공정분배금액이랑 관련없이 충전할 때 더해줄 공정분배금액
   *
   * @param amount 충전 요청 금액
   * @param users  List<RechargeUser>
   * @return 모든 그룹원 1/n
   */
  private Integer getPairAmount(Integer amount, List<RechargeUser> users) {
    return amount / users.size();
  }

  /**
   * 충전 요청 받은 그룹원 joined 상태 true로 바꿔 주는 로직
   *
   * @param rechargeUsers 충전 요청 체크된 그룹원
   */
  private void joinRecharge(List<RechargeUser> rechargeUsers) {
    for (RechargeUser rechargeUser : rechargeUsers) {
      rechargeUser.changeJoinedState(true);
    }
  }

  //-------------------------------------------

  /**
   * 충전 유저들 중에서 양심금 마이너스인 유저들만 담아서 리턴
   *
   * @param rechargeUsers Recharge -> RechargeUsers
   * @return 양심에 금이간 회원들
   */
  public List<RechargeUser> checkRechargeUsersPairAmount(List<RechargeUser> rechargeUsers) {
    return rechargeUsers.stream()
        .filter(this::checkOverPairAmount)
        .toList();
  }

  /**
   * 회원 아이디로 양심금 넘었는 지 확인 하는 로직, 기본 값은 0이므로 첫 결제 때는 양심금 따로 확인 안하게 함 넘었을 경우 컨트롤러에서 다른 경로로 보내주면 되려나
   *
   * @param rechargeUser
   * @return 양심금 넘었는지 true/false
   */
  private boolean checkOverPairAmount(RechargeUser rechargeUser) {
    GroupUser groupUser = groupUserRepository.findByUserUsername(
        rechargeUser.getGroupUser().getUser().getUsername());

    if (groupUser.getPairShareAmount() != 0) {
      return groupUser.getPairShareAmount() < 0;
    }
    return true;
  }

  /**
   * 패널티(양심금 오바된 애들 돈 더 내게)에 따른 그룹원 별 충전 금액을 부여 하는 로직 부여된 채로 db에 반영
   *
   * @param seq                      Recharge seq
   * @param requestPairAmountPenalty 패널티 부여할 회원들의 아이디를 가지고 있는 폼
   */
  @Transactional
  public void requestPenalty(Long seq, RequestPairAmountPenalty requestPairAmountPenalty) {
    Recharge recharge = getRecharge(seq);

    List<RechargeUser> rechargeUsers = convertUsernamesAndRechargeSeqToRechargeUsers(
        requestPairAmountPenalty.usernames(), seq);

    joinPenalty(recharge, rechargeUsers);

    getRechargeAmount(recharge.getRechargeUsers(), recharge.getRechargeAmount());


  }

  /**
   * 패널티를 부여하는 로직, 상태만 바꿈
   *
   * @param rechargeUsers 패널티를 부여할 회원들
   */

  private void joinPenalty(Recharge recharge,List<RechargeUser> rechargeUsers) {
    for (RechargeUser user : rechargeUsers) {
      user.changePenaltyState(true);
      recharge.addPairAmount(user.getGroupUser().getPairShareAmount());
    }
  }

  /**
   * 인당 충전 금액 구하는 로직, 사전에 rechargeUsers의 penaltyPairAmount의 상태가 정해져 있어야 함
   *
   * @param rechargeUsers Recharge -> RechargeUsers
   * @param amount        Recharge -> RechargeAmount
   */
  private void getRechargeAmount(List<RechargeUser> rechargeUsers, Integer amount) {
    List<RechargeUser> joinedUsers = getJoinedUsers(rechargeUsers);
    int sharedAmount = amount / joinedUsers.size();

    for (RechargeUser rechargeUser : joinedUsers) {

      if (rechargeUser.isPenaltyPairAmount()) {
        rechargeUser.getRechargeAmount(
            sharedAmount + Math.abs(rechargeUser.getGroupUser().getPairShareAmount()));
      } else {
        rechargeUser.getRechargeAmount(sharedAmount);
      }

    }
  }

  //------------------------------------------------------------------------------------

  /**
   * 충전하기 버튼 눌렀을 때 호출되는 로직 만약 모든 회원이 충전했을 경우 그룹카드에 잔고가 올라가고 충전내역을 갖게된다.
   *
   * @param username Username
   * @param seq      Recharge Seq
   */
  @Transactional
  public void processRecharge(String username, Long seq) {
    RechargeUser rechargeUser = convertUsernameAndSeqToRechargeUser(username, seq);
    Recharge recharge = getRecharge(seq);
    rechargeUser.changePayedState(true);
    // 주의
    getRechargeAmount(recharge.getRechargeUsers(), recharge.getRechargeAmount());

    if (checkRechargeFinished(seq)) {
      recharge.rechargeFinishedDate();
      recharge.changeState(State.FINISHED);
      groupCardRepository.findByGroupName(
          groupUserRepository.findByUserUsername(username).getGroupCard()
              .getGroupName()).addCharge(recharge.getRechargeAmount());

      recharge.getRechargeUsers().stream().filter(RechargeUser::isPayed)
          .map(RechargeUser::getGroupUser).forEach(s -> {
            s.changeRecentChargedDate();
            s.addRechargedAmount(rechargeUser.getRechargeAmount());
          });

      // 패널티 받은 유저들 양심금 초기화
      resetPairSharedAmount(recharge.getRechargeUsers());

      // 양심금 더해줌
      addPairSharedAmount(recharge);

    }

  }

  /**
   * 패널티 받은 사람들 양심금 0처리 해주는 로직
   *
   * @param rechargeUsers Recharge -> RechargeUsers
   */
  private void resetPairSharedAmount(List<RechargeUser> rechargeUsers) {
    List<GroupUser> payedPenaltyUsers = getPayedUsers(getPenaltyUsers(rechargeUsers)).stream()
        .map(RechargeUser::getGroupUser).toList();

    for (GroupUser s : payedPenaltyUsers) {
      s.changePairSharedAmount(0);
    }

  }

  /**
   * 공정금액 더하는 로직
   *
   * @param rechargeUsers
   * @param pairAmount
   */
  private void addPairSharedAmount(Recharge recharge) {

    //양심금 지급
    recharge.getRechargeUsers().stream().map(RechargeUser::getGroupUser)
        .forEach(s -> s.addPairSharedAmount(recharge.getPairAmount()));

  }


  /**
   * 모든 충전이 완료 되었는 지 확인 하는 로직, 전부 완료될 경우 true를 반환함
   *
   * @param seq Recharge Seq
   * @return true false
   */
  private boolean checkRechargeFinished(Long seq) {
    return getJoinedUsers(getRecharge(seq).getRechargeUsers()).stream()
        .allMatch(RechargeUser::isPayed);
  }

  /**
   * 유저가 결제했는지 확인하는 로직
   *
   * @return
   */
  public boolean checkUserPayed(String username, Long rechargeSeq) {
    return convertUsernameAndSeqToRechargeUser(username, rechargeSeq).isPayed();

  }

  @Transactional
  public void cancelRecharge(Long seq) {
    Recharge recharge = rechargeRepository.findById(seq).orElseThrow();

    if (recharge.getRechargeUsers().stream().noneMatch(RechargeUser::isPayed)) {
      //아직 결제한 사람이 없을 때 -> 삭제 가능할 때
      recharge.getRechargeUsers().forEach(s -> s.requestRecharge(null));
      rechargeRepository.delete(recharge);
    } else {
      throw new IllegalArgumentException("이미 충전한 회원이 존재합니다!");
    }
  }


}

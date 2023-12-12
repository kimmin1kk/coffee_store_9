package com.db.coffeestore9.group.service;

import com.db.coffeestore9.group.domain.GroupCard;
import com.db.coffeestore9.group.repository.GroupCardRepository;
import com.db.coffeestore9.user.domain.GroupUser;
import com.db.coffeestore9.user.repository.GroupUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupUserService {

  private final GroupCardRepository groupCardRepository;
  private final GroupUserRepository groupUserRepository;

  /**
   * 사용자 아이디를 통해 그룹을 찾는 로직
   *
   * @param username
   * @return
   */
  public GroupCard getMyGroup(String username) {
    return groupCardRepository.findByGroupName(
        groupUserRepository.findByUserUsername(username).getGroupCard().getGroupName());
  }

  /**
   * 비활성화된 그룹 활성화 요청하는 로직
   * @param username
   */
  @Transactional
  public void requestGroupActive(String username) {
    groupUserRepository.findByUserUsername(username).changeGroupActiveRequested(true);
  }

  /**
   * 그룹 초대 수락하는 로직
   * @param username
   */
  @Transactional
  public void acceptGroupJoin(String username) {
    groupUserRepository.findByUserUsername(username).changeUserAccepted(true);
  }

  /**
   * 그룹 초대 거절하는 로직
   * @param username
   */
  @Transactional
  public void rejectGroupJoin(String username) {
    groupUserRepository.delete(groupUserRepository.findByUserUsername(username));
  }

  /** 그룹생성 요청 후, 승인한 유저가 3명 이상일 경우 createActive를 true로 바꿔주는 로..직
   *
   * @param groupCard
   */
  public void checkValidation(GroupCard groupCard) {
    if (groupCard.getGroupUsers().stream().filter(GroupUser::isUserAccepted).count() >= 3) {
      groupCard.changeCreateActive(true);
    }
  }
}

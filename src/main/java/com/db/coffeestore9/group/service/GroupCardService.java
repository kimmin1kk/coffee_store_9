package com.db.coffeestore9.group.service;

import com.db.coffeestore9.group.domain.GroupCard;
import com.db.coffeestore9.group.repository.GroupCardRepository;
import com.db.coffeestore9.user.domain.GroupUser;
import com.db.coffeestore9.user.repository.GroupUserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupCardService {

  private final GroupCardRepository groupCardRepository;
  private final GroupUserRepository groupUserRepository;

  /**
   * 그룹이 활성화 상태인지 확인하는 로직
   *
   * @param groupCard
   * @return
   */
  public boolean checkGroupCardActiveState(GroupCard groupCard) {
    return groupCard.isActive();
  }

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
   * 그룹원 찾는 로직
   *
   * @param groupCard 그룹원을 찾으려는 그룹
   * @return 그룹원들
   */
  public List<GroupUser> getGroupUsers(GroupCard groupCard) {
    return groupUserRepository.findGroupUsersByGroupCardSeq(groupCard.getSeq());
  }

  @Transactional
  public void requestGroupActive(String username) {
    groupUserRepository.findByUserUsername(username).changeGroupActiveRequested(true);
  }


  /**
   * 활성화 요청이 과반수 이상 되었는지 확인하는 로직, 과반수가 넘었을 경우 그룹카드를 활성화 시키면서 그룹원들의 활성화요청상태를 false로 초기화 시킴
   *
   * @param groupCard
   */
  @Transactional
  public void isMajorityActivationRequested(GroupCard groupCard) {
    float totalUsersCount = groupCard.getGroupUsers().size();

    float acceptedUsersCount = groupCard.getGroupUsers().stream()
        .filter(GroupUser::isGroupActiveRequested)
        .count();

    // 과반수 이상 되었을 때
    if (totalUsersCount / 2 <= acceptedUsersCount) {
      groupCard.changeActive(true);

      for (GroupUser groupUser : groupCard.getGroupUsers()) {
        groupUser.changeGroupActiveRequested(true);
      }

    }
  }

}

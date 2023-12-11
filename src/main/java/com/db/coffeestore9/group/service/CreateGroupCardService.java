package com.db.coffeestore9.group.service;

import com.db.coffeestore9.group.common.CreateGroupCardForm;
import com.db.coffeestore9.group.domain.GroupCard;
import com.db.coffeestore9.group.repository.GroupCardRepository;
import com.db.coffeestore9.rank.domain.TotalRanking;
import com.db.coffeestore9.rank.repository.TotalRankingRepository;
import com.db.coffeestore9.user.domain.GroupUser;
import com.db.coffeestore9.user.domain.User;
import com.db.coffeestore9.user.repository.GroupUserRepository;
import com.db.coffeestore9.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateGroupCardService {

  private final GroupCardRepository groupCardRepository;
  private final GroupUserRepository groupUserRepository;
  private final UserRepository userRepository;
  private final TotalRankingRepository totalRankingRepository;

  /**
   * 그룹카드 생성하는 로직 (내부적으로 처리 하는 메서드들은 private 접근 제한 자로 선언해 둠 실제 생성할 떄는 이 로직만 호출 하면 됨)
   *
   * @param createGroupCardForm
   */
  @Transactional
  public void processGenerateGroupCard(CreateGroupCardForm createGroupCardForm) {

    if (checkValidation(createGroupCardForm)) {
      GroupCard groupCard = createGroupCard(createGroupCardForm.groupName());
      createGroupAdmin(createGroupCardForm.adminUsername(), groupCard);
      createGroupUser(createGroupCardForm.usernames(), groupCard);
      createTotalRanking(groupCard);
    } else {
      throw new IllegalArgumentException(
          "추가한 유저 중 이미 그룹이 존재하는 인원이 포함되어 있거나 그룹을 생성하기 위한 최소 인원수가 충족되지 않았습니다.");
    }
  }

  /**
   * 그룹카드를 만드는 로직
   *
   * @param groupName
   * @return
   */
  private GroupCard createGroupCard(String groupName) {
    return groupCardRepository.save(GroupCard.builder().groupName(groupName).build());
  }

  private void createGroupAdmin(String username, GroupCard groupCard) {
    User user = userRepository.findByUsername(username);
    GroupUser groupUser = GroupUser.builder().user(user).administrator(true).groupCard(groupCard)
        .build();

    user.getGroup(groupUserRepository.save(groupUser));
    groupCard.addGroupUser(groupUser);
  }

  private void createGroupUser(List<String> usernames, GroupCard groupCard) {
    usernames.stream()
        .map(userRepository::findByUsername)
        .map(s -> GroupUser.builder().user(s).groupCard(groupCard).build())
        .forEach(groupCard::addGroupUser);
    usernames.stream()
        .map(userRepository::findByUsername)
        .forEach(s -> s.getGroup(
            groupUserRepository.save(GroupUser.builder().user(s).groupCard(groupCard).build())));
  }

  private void createTotalRanking(GroupCard groupCard) {
    groupCard.getTotalRanking(
        totalRankingRepository.save(TotalRanking.builder().groupCard(groupCard).build()));
  }

  /**
   * 유효성 검사 로직 추가한 사용자 수가 3명 이상이 아니거나 이미 그룹이 있는 인원이 있을 경우 IllegalArgumentException 던짐
   *
   * @param createGroupCardForm
   * @return true or false
   */
  private boolean checkValidation(CreateGroupCardForm createGroupCardForm) {

    if (createGroupCardForm.usernames().stream()
        .map(userRepository::findByUsername)
        .anyMatch(s -> s.getGroupUser() != null)
    ) {
      return false;
    }

    return createGroupCardForm.usernames().stream()
        .map(userRepository::findByUsername)
        .filter(User::isEnabled)
        .count() >= 3;
  }

}

package com.db.coffeestore9.group.repository;

import com.db.coffeestore9.group.domain.GroupCard;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GroupCardRepository extends JpaRepository<GroupCard, Long> {

  GroupCard findByGroupName(String groupName);

  @Query("SELECT gc FROM GroupCard gc JOIN gc.groupUsers gu JOIN gu.user u WHERE u.username = :username")
  GroupCard findGroupCardByUserUsername(String username);

  // 사용금액 낮은 순서대로
    List<GroupCard> findByActiveTrueOrderByMonthlyUsedChargeAsc();

  // 사용금액 높은 순서대로
  List<GroupCard> findByActiveTrueOrderByMonthlyUsedChargeDesc();


}
package com.db.coffeestore9.user.repository;

import com.db.coffeestore9.user.domain.GroupUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupUserRepository extends JpaRepository<GroupUser, Long> {

  List<GroupUser> findGroupUsersByGroupCardSeq(Long seq);

  GroupUser findByUserUsername(String username);

}
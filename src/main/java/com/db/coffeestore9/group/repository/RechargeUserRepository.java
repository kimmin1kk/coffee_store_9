package com.db.coffeestore9.group.repository;

import com.db.coffeestore9.group.domain.RechargeUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RechargeUserRepository extends JpaRepository<RechargeUser, Long> {
  RechargeUser findTopByGroupUserUserUsernameAndGroupUserGroupCardSeqOrderByCreatedDateDesc(String username,Long seq);

  List<RechargeUser> findRechargeUsersByGroupUserUserUsernameAndGroupUserGroupCardSeq(String username,Long seq);

  RechargeUser findByGroupUserUserUsernameAndRechargeSeq(String username, Long seq);

}
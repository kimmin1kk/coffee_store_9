package com.db.coffeestore9.group.repository;

import com.db.coffeestore9.group.domain.RechargeUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RechargeUserRepository extends JpaRepository<RechargeUser, Long> {


  RechargeUser findRechargeUsersByGroupUserUserUsernameAndGroupUserGroupCardSeq(String username,Long seq);

  RechargeUser findByGroupUserUserUsernameAndRechargeSeq(String username, Long seq);

}
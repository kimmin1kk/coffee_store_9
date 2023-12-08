package com.db.coffeestore9.user.service;

import com.db.coffeestore9.user.common.RegistrationForm;
import com.db.coffeestore9.user.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class UserServiceTest {

  @Autowired
  private UserService userService;


  @Test
  @Transactional
  void processRegistration() {
    //given
    RegistrationForm form = new RegistrationForm("testUser1", "1234213Zx!", "김김김", "테스트유저1");
    //when
    userService.processRegistration(form);
    User user = userService.findByUsername(form.username());
    //then
    Assertions.assertNotNull(user);
  }
}
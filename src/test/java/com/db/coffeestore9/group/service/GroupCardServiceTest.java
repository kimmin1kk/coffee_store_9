package com.db.coffeestore9.group.service;

import static org.junit.jupiter.api.Assertions.*;

import com.db.coffeestore9.group.common.CreateGroupCardForm;
import com.db.coffeestore9.group.repository.GroupCardRepository;
import com.db.coffeestore9.user.common.RegistrationForm;
import com.db.coffeestore9.user.repository.GroupUserRepository;
import com.db.coffeestore9.user.service.UserService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GroupCardServiceTest {


  @Autowired
  private UserService userService;

  @Autowired
  private CreateGroupCardService createGroupCardService;

  @Autowired
  private GroupCardRepository groupCardRepository;

  @Autowired
  private GroupUserRepository groupUserRepository;

  @BeforeAll
  void setUp() {
    userService.processRegistration(new RegistrationForm("Test11111","1234567ZxX!","김밥","테스트유저1"));
    userService.processRegistration(new RegistrationForm("Test22222","1234567ZxX!","김치","테스트유저2"));
    userService.processRegistration(new RegistrationForm("Test33333","1234567ZxX!","감자","테스트유저3"));
    userService.processRegistration(new RegistrationForm("Test44444","1234567ZxX!","고구마","테스트유저4"));
    userService.processRegistration(new RegistrationForm("Test55555","1234567ZxX!","호구마","테스트유저5"));
  }

  @Test
  @Transactional
  void processGenerateGroupCard() {
    //given
    List<String> testList = Arrays.asList("Test22222", "Test33333", "Test44444");
    CreateGroupCardForm createGroupCardForm = new CreateGroupCardForm("Test11111", "테스트그룹1", testList);

    //when
    createGroupCardService.processGenerateGroupCard(createGroupCardForm);

    //then
    assertNotNull(groupCardRepository.findByGroupName("테스트그룹1"));
    assertTrue(userService.findByUsername("Test11111").getGroupUser().isAdministrator());
  }
}
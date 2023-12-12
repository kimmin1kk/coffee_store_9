package com.db.coffeestore9.group.controller;

import com.db.coffeestore9.group.common.CreateGroupCardForm;
import com.db.coffeestore9.group.service.CreateGroupCardService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/create/group")
public class CreateGroupCardController {

  private final CreateGroupCardService createGroupCardService;


  /**
   * /group/createForm 경로에 createGroupCardForm 이랑 맞는 폼하나 만들어서 연결해주면 됨
   * Principal, Model 부분은 AOP 관련 세팅해둔 거 때문에 해둔거니까 크게 상관 안해도 되는 부분
   * @param principal
   * @param model
   * @return
   */
  @GetMapping
  public String showCreateForm(Principal principal, Model model) {
    return "group/createForm";
  }

  /**
   * 파라미터 -> GetMapping 경로에 만들어둔 폼
   * @param groupCardForm
   * @return
   */
  @PostMapping
  public String createGroupCard(@ModelAttribute CreateGroupCardForm groupCardForm) {
    createGroupCardService.processGenerateGroupCard(groupCardForm);

    return "redirect:/";
  }

}

package com.db.coffeestore9.group.controller;

import com.db.coffeestore9.group.domain.GroupCard;
import com.db.coffeestore9.group.service.GroupCardService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/group")
public class GroupCardController {

  private final GroupCardService groupCardService;


  /**
   * 그룹이 활성화된 상태라면 마이그룹페이지로 이동 비활성화 상태라면 활성화 요청 페이지로 이동
   *
   * @param principal
   * @param model
   * @return
   */
  @GetMapping
  public String myGroupCardPage(Principal principal, Model model) {
    GroupCard groupCard = groupCardService.getMyGroup(principal.getName());
    if (groupCardService.checkGroupCardActiveState(
        groupCard)) {
      model.addAttribute("groupCard", groupCard);
      return "group/myGroup";

    }

    model.addAttribute("groupUsers", groupCardService.getGroupUsers(groupCard));
    return "group/disabledGroup";
  }


  @PostMapping("/request/active")
  public String requestActive(Principal principal, Model model) {
    GroupCard groupCard = groupCardService.getMyGroup(principal.getName());

    groupCardService.requestGroupActive(principal.getName());

    groupCardService.isMajorityActivationRequested(groupCard);

    return "redirect:/";
  }


}

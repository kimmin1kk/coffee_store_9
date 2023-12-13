package com.db.coffeestore9.group.controller;

import com.db.coffeestore9.group.domain.GroupCard;
import com.db.coffeestore9.group.service.GroupCardService;
import com.db.coffeestore9.group.service.GroupUserService;
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
  private final GroupUserService groupUserService;
  private static final String DEFAULT_REDIRECT = "redirect:/";


  /**
   * 그룹카드 설명하는 페이지로 연결
   *
   * @return
   */
  @GetMapping
  public String groupInfoPage(Model model, Principal principal) {
    return "/group/groupinfo";
  }

  /**
   * 그룹이 활성화된 상태라면 마이그룹페이지로 이동 비활성화 상태라면 활성화 요청 페이지로 이동
   *
   * @param principal
   * @param model
   * @return
   */
  @GetMapping("/myGroup")
  public String myGroupCardPage(Model model, Principal principal) {
    GroupCard groupCard = groupUserService.getMyGroup(principal.getName());
    if (groupCardService.checkGroupCardActiveStateAndCreateActive(
        groupCard)) {
      //그룹 활성화 상태 + 3명 이상 들어온 상태
      model.addAttribute("groupCard", groupCard);
      return "/group/myGroup";
    } else if (!groupCard.isActive()) {
      //그룹 비활성화 상태
      model.addAttribute("groupUsers", groupCardService.getGroupUsers(groupCard));
      return "/group/disabledGroup";
    } else if (!groupCard.isCreateActive()) {
      //그룹 인원 3명 이상 들어 오지 않은 상태
      model.addAttribute("groupUsers", groupCardService.getGroupUsers(groupCard));
      return "/group/waitForCreateGroup";
    } else {
      throw new IllegalArgumentException("잘못된 접근입니다.");
    }
  }


  @GetMapping("/myGroup/history")
  public String getHistory(Model model, Principal principal) {
    model.addAttribute("histories",
        groupCardService.findGroupChargeHistory(
            groupUserService.getMyGroup(principal.getName()).getSeq()));
    return "/group/purchaseHistory";
  }

  @PostMapping("/request/active")
  public String requestActive(Model model, Principal principal) {
    GroupCard groupCard = groupUserService.getMyGroup(principal.getName());

    groupUserService.requestGroupActive(principal.getName());

    groupCardService.isMajorityActivationRequested(groupCard);

    return DEFAULT_REDIRECT;
  }

  @PostMapping("/accept/joinGroup")
  public String joinGroup(Model model, Principal principal) {
    GroupCard groupCard = groupUserService.getMyGroup(principal.getName());

    groupUserService.acceptGroupJoin(principal.getName());

    groupUserService.checkValidation(groupCard);

    if (groupCard.isCreateActive()) {
      //필요 회원 수 충족해서 그룹이 사용가능해진 상태
      return DEFAULT_REDIRECT + "group/myGroup";
    }
    return DEFAULT_REDIRECT;

  }

  @PostMapping("/reject/joinGroup")
  public String rejectJoinGroup(Model model, Principal principal) {

    groupUserService.rejectGroupJoin(principal.getName());

    return DEFAULT_REDIRECT;

  }

}

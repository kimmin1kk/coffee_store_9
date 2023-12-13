package com.db.coffeestore9.group.controller;

import com.db.coffeestore9.group.domain.GroupCard;
import com.db.coffeestore9.group.service.GroupCardService;
import com.db.coffeestore9.group.service.RechargeService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manageGroup")
public class ManageGroupController {

  private final GroupCardService groupCardService;
  private final RechargeService rechargeService;

  @GetMapping("/groupInfo")
  public String getGroupInfo(Model model, Principal principal) {
    GroupCard groupCard = groupCardService.getGroupCard(principal.getName());

    model.addAttribute("groupcard", groupCard);

    model.addAttribute("groupUsers", groupCard.getGroupUsers());

    return "/manageGroup/groupInfo";
  }

  @GetMapping("/rechargeHistory")
  public String getRechargeHistories(Model model, Principal principal) {
    GroupCard groupCard = groupCardService.getGroupCard(principal.getName());

    model.addAttribute("recharges", rechargeService.getRechargeHistory(groupCard));


    return "/manageGroup/rechargeHistory";
  }
}

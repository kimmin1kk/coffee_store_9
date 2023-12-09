package com.db.coffeestore9.group.controller;

import com.db.coffeestore9.group.common.RequestPairAmountPenalty;
import com.db.coffeestore9.group.common.RequestRechargeForm;
import com.db.coffeestore9.group.service.GroupCardService;
import com.db.coffeestore9.group.service.RechargeService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/recharge")
@RequiredArgsConstructor
public class RechargeController {

  private final RechargeService rechargeService;
  private final GroupCardService groupCardService;

  @GetMapping("/request/form")
  public String rechargeForm(Principal principal, Model model) {
    model.addAttribute("groupUsers",
        groupCardService.getGroupUsers(groupCardService.getMyGroup(principal.getName())));
    return "/recharge/form";
  }

  @PostMapping("/request/form")
  public String processRecharge(Principal principal, Model model, @ModelAttribute
  RequestRechargeForm requestRechargeForm) {
    Long seq = rechargeService.requestRecharge(requestRechargeForm).getSeq();

    return "redirect:/recharge/pairAmount/" + seq;
  }

  /**
   * @param seq 생성된 Recharge의 seq
   * @return
   */
  @GetMapping("/request/pairAmount/{seq}")
  public String pairAmountPenaltyCheckForm(Principal principal, Model model,
      @PathVariable("seq") Long seq) {
    model.addAttribute("rechargeUsers", rechargeService.checkRechargeUsersPairAmount(
        rechargeService.getRecharge(seq).getRechargeUsers()));

    return "/recharge/pairForm";
  }

  @PostMapping("/request/pairAmount/{seq}")
  public String pairAmountPenaltyCheck(Principal principal, Model model,
      @PathVariable("seq") Long seq, @ModelAttribute
  RequestPairAmountPenalty requestPairAmountPenalty) {

    rechargeService.requestPenalty(seq, requestPairAmountPenalty);

    return "redirect:/";
  }

  @PostMapping("/request/pay/{seq}")
  public String processingPayment(Principal principal, Model model, @PathVariable("seq") Long seq) {
    rechargeService.processRecharge(principal.getName(), seq);

    return "redirect:/";
  }
}

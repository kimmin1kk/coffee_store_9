package com.db.coffeestore9.group.controller;

import com.db.coffeestore9.global.common.State;
import com.db.coffeestore9.group.common.RequestPairAmountPenalty;
import com.db.coffeestore9.group.common.RequestRechargeForm;
import com.db.coffeestore9.group.domain.GroupCard;
import com.db.coffeestore9.group.domain.RechargeUser;
import com.db.coffeestore9.group.service.GroupCardService;
import com.db.coffeestore9.group.service.GroupUserService;
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
@RequestMapping("/manageGroup/recharge")
@RequiredArgsConstructor
public class RechargeController {

  private final RechargeService rechargeService;
  private final GroupCardService groupCardService;
  private final GroupUserService groupUserService;

  @GetMapping("/request/form")
  public String rechargeForm(Model model, Principal principal) {
    model.addAttribute("groupUsers",
        groupCardService.getAcceptedGroupUsers(groupUserService.getMyGroup(principal.getName())));
    return "/recharge/form";
  }

  /**
   * @return
   */
  @GetMapping("/form")
  public String rechargeBasicForm(Model model, Principal principal) {
    GroupCard groupCard = groupCardService.getGroupCard(principal.getName());
    //진행중인 충전이 있는가(하나라도 있는가랑 동일하나 한 번에 하나만 가능해야하니까..)
    if (rechargeService.getRechargeHistory(groupCard).stream()
        .anyMatch(s -> s.getState() == State.ON_PROGRESS)) {
      // 현재 진행중인 충전
      model.addAttribute("joinedUsers",
          rechargeService.getOnProgressRecharge(rechargeService.getRechargeHistory(groupCard))
              .getRechargeUsers().stream().filter(
                  RechargeUser::isJoined).toList());
      model.addAttribute("onProgressRecharge", rechargeService.getOnProgressRecharge(
          rechargeService.getRechargeHistory(groupCard)));

      model.addAttribute("chargingAmount",
          rechargeService.getOnProgressRecharge(rechargeService.getRechargeHistory(groupCard))
              .getRechargeUsers().stream().filter(
                  RechargeUser::isJoined).filter(RechargeUser::isPayed)
              .mapToInt(RechargeUser::getRechargeAmount).sum());
    }

    return "recharge/basicForm";
  }

  @PostMapping("/request")
  public String processRecharge(Model model, Principal principal, @ModelAttribute
  RequestRechargeForm requestRechargeForm) {
    Long seq = rechargeService.requestRecharge(requestRechargeForm).getSeq();

    if (rechargeService.checkUsersPairAmount(rechargeService.getRecharge(seq))) {
      //양심금 오바된 사람 있을 때
      return "redirect:/manageGroup/recharge/pairAmount/" + seq;
    }
    //양심금 오바된 사람 없을 때
    return "redirect:/manageGroup/recharge/form";

  }

  /**
   * @param seq 생성된 Recharge의 seq
   * @return
   */
  @GetMapping("pairAmount/{seq}")
  public String pairAmountPenaltyCheckForm(Model model, Principal principal,
      @PathVariable("seq") Long seq) {
    model.addAttribute("rechargeUsers", rechargeService.checkRechargeUsersPairAmount(
        rechargeService.getRecharge(seq).getRechargeUsers()));

    return "/recharge/pairForm";
  }

  @PostMapping("/pairAmount/{seq}")
  public String pairAmountPenaltyCheck(Model model, Principal principal,
      @PathVariable("seq") Long seq, @ModelAttribute
  RequestPairAmountPenalty requestPairAmountPenalty) {

    rechargeService.requestPenalty(seq, requestPairAmountPenalty);

    return "redirect:/manageGroup/recharge/form";
  }

  @PostMapping("/pay/{seq}")
  public String processingPayment(Model model, Principal principal, @PathVariable("seq") Long seq) {
    rechargeService.processRecharge(principal.getName(), seq);

    return "redirect:/";
  }
}

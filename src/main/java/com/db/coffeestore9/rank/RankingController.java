package com.db.coffeestore9.rank;

import com.db.coffeestore9.rank.common.CreateRankingForm;
import com.db.coffeestore9.rank.service.RankingService;
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
@RequiredArgsConstructor
@RequestMapping("/ranking")
public class RankingController {

  private final RankingService rankingService;

  @GetMapping("/info")
  public String rankingInfo(Model model, Principal principal) {

    model.addAttribute("rankings", rankingService.getAllRankings());

    return "ranking/info";
  }

  @GetMapping("/form")
  public String createRankingForm(Model model, Principal principal) {

    return "/ranking/createForm";
  }

  @PostMapping("/create")
  public String createRanking(Model model, Principal principal,
      @ModelAttribute CreateRankingForm createRankingForm) {
    rankingService.createRankingSchedule(createRankingForm);

    return "redirect:/ranking/info";
  }

  @PostMapping("/start/{seq}")
  public String startRanking(Model model, Principal principal, @PathVariable("seq") Long rankingSeq, Integer yymm) {
    rankingService.startRankingSchedule(rankingSeq, yymm);
    return "redirect:/ranking/info";
  }

  @PostMapping("/end/{seq}")
  public String endRanking(Model model, Principal principal, @PathVariable("seq") Long rankingSeq, Integer yymm) {
    rankingService.processRankingEnds(rankingSeq);

    return "redirect:/ranking/info";
  }



}

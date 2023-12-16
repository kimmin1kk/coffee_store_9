package com.db.coffeestore9.rank.controller;

import com.db.coffeestore9.group.service.GroupCardService;
import com.db.coffeestore9.rank.common.CreateRankingForm;
import com.db.coffeestore9.rank.service.RankInfoService;
import com.db.coffeestore9.rank.service.RankingService;
import com.db.coffeestore9.rank.service.TotalRankingService;
import java.security.Principal;
import java.sql.Timestamp;
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
  private final TotalRankingService totalRankingService;
  private final GroupCardService groupCardService;
  private final RankInfoService rankInfoService;

  /**
   * 현재 등록되어 있는 랭킹 이벤트들을 전부 볼 수 있는 페이지로 연결해주는 컨트롤러
   *
   * @return
   */
  @GetMapping("/info")
  public String rankingInfo(Model model, Principal principal) {

    //현재 진행중인 랭킹
    if (rankingService.getAllRankings() != null) {
      model.addAttribute("onProgressRanking", rankingService.getActiveRanking());
      model.addAttribute("joinedGroupCount",
          (long) rankingService.getActiveRanking().getRankingInfos().size());
      model.addAttribute("top3", rankInfoService.getRankingTop3(rankingService.getActiveRanking()));
      model.addAttribute("myGroupInfo",
          rankInfoService.getMyRankInfo(rankingService.getActiveRanking(), principal.getName()));
    }


    // 종료된 랭킹들
    model.addAttribute("rankings", rankingService.getFinishedRankings());

    // 내 그룹 통계
    model.addAttribute("myGroupRanking", totalRankingService.getGroupTotalRanking(
        groupCardService.getGroupCard(principal.getName())));



    return "ranking/info";
  }

  /**
   * 랭킹 등록 폼으로 연결해주는 컨트롤러
   *
   * @return
   */
  @GetMapping("/form")
  public String createRankingForm(Model model, Principal principal) {

    return "/ranking/createForm";
  }

  @GetMapping("/manageRanking")
  public String manageRankList(Model model, Principal principal) {

    model.addAttribute("notYetRankings", rankingService.getNotYetRankings());
    if (rankingService.getAllRankings() != null) {
      model.addAttribute("onProgressRanking", rankingService.getActiveRanking());
    }
    model.addAttribute("finishedRankings", rankingService.getFinishedRankings());

    return "/ranking/manageRanking";
  }

  /**
   * @param createRankingForm (yymm, eventKind(Kind), eventName)
   * @return
   */
  @PostMapping("/create")
  public String createRanking(Model model, Principal principal,
      @ModelAttribute CreateRankingForm createRankingForm) {
    rankingService.createRankingSchedule(createRankingForm);

    return "redirect:/ranking/info";
  }

  /**
   * 실제로 스케줄러 돌리기엔 시연하기 너무 힘듬 -> 강제시작
   *
   * @param rankingSeq
   * @param yymm
   * @return
   */
  @PostMapping("/start/{seq}")
  public String startRanking(Model model, Principal principal, @PathVariable("seq") Long rankingSeq,
      Timestamp yymm) {
    rankingService.startRankingSchedule(rankingSeq, yymm);
    return "redirect:/ranking/manageRanking";
  }

  /**
   * 실제로 스케줄러 돌리기엔 시연하기 너무 힘듬 2 -> 강제종료
   *
   * @param rankingSeq
   * @param yymm
   * @return
   */
  @PostMapping("/end/{seq}")
  public String endRanking(Model model, Principal principal, @PathVariable("seq") Long rankingSeq,
      Integer yymm) {
    rankingService.processRankingEnds(rankingSeq);

    return "redirect:/ranking/manageRanking";
  }


}

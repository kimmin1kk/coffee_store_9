package com.db.coffeestore9.order.controller;

import com.db.coffeestore9.order.domain.OrderContent;
import com.db.coffeestore9.order.repository.OrderContentRepository;
import com.db.coffeestore9.order.service.OrderContentService;
import java.security.Principal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderContentController {

  private final OrderContentService orderContentService;
  private final OrderContentRepository orderContentRepository;

  @GetMapping("/shopping-cart")
  public String shoppingCart(Model model, Principal principal) {
    orderContentService.getOrders(principal.getName());
    model.addAttribute("orders", orderContentService.findOrders(principal.getName()));
    if (orderContentService.findOrders(principal.getName()).getOrderContentList() == null) {
      model.addAttribute("totalPrice", 0);
    } else {
      model.addAttribute("totalPrice", orderContentService.findTotalPrice(principal.getName()));
    }
    return "shop/shoppingCart";

  }

  /**
   * 장바구니에 담기
   *
   * @param seq   상품 PK
   * @param count 상품 수
   * @return
   */
  @PostMapping("/add-to-cart/{seq}")
  public String addToCart(Model model, Principal principal, @PathVariable("seq") Long seq,
      @RequestParam(value = "count", required = false) Integer count) {
    if (count == null) {
      count = 1; // count가 null일 경우 기본값으로 1 할당
    }

    orderContentService.addProductToCart(seq, principal.getName(), count);
    return "redirect:/";
  }

  @GetMapping("/delete-product-from-cart/{seq}")
  public String deleteProductFromCart(@PathVariable("seq") Long seq, Principal principal) {
    orderContentRepository.deleteById(seq);
    return "redirect:/shopping-cart";
  }

  @PostMapping("edit-product-count-from-cart/{seq}")
  public String editProductCountFromCart(@RequestParam("count") Integer count,
      @PathVariable("seq") Long seq, Principal principal) {
    Optional<OrderContent> orderContentOptional = orderContentRepository.findById(seq);
    if (orderContentOptional.isPresent()) {
      OrderContent orderContent = orderContentOptional.get();
      orderContent.updateCount(count);
      orderContentRepository.save(orderContent);
    }
    return "redirect:/shopping-cart";
  }
}

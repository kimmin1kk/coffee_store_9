package com.db.coffeestore9.order.controller;

import com.db.coffeestore9.order.common.OrderPageForm;
import com.db.coffeestore9.order.common.PaymentMethod;
import com.db.coffeestore9.order.service.OrderContentService;
import com.db.coffeestore9.order.service.OrdersService;
import java.security.Principal;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {

  private static final String ORDER_ATTRIBUTE = "orders";


  private final OrdersService ordersService;
  private final OrderContentService orderContentService;

  /**
   * 장바구니에서 결제하기를 클릭했을 때 호출되는 컨트롤러
   *
   * @param model
   * @param principal
   * @return
   */
  @GetMapping("/order-page")
  public String orderPage(Model model, Principal principal) {
    model.addAttribute(ORDER_ATTRIBUTE, orderContentService.findOrders(principal.getName()));
    return "shop/orderPage";
  }

  @PostMapping("/order-page")
  public String orderPage(Model model, Principal principal,
      @ModelAttribute OrderPageForm orderPageForm) {

    ordersService.confirmOrder(orderPageForm,
        orderContentService.findOrders(principal.getName()));

    return "redirect:/";
  }

  @GetMapping("/order-history")
  public String orderHistoryPage(Model model, Principal principal) {
    model.addAttribute("orderedCarts", ordersService.getOrderedCarts(principal.getName()));
    return "account/orderHistory";
  }

  @GetMapping("/order-list")
  public String orderList(Model model, Principal principal) {
    model.addAttribute(ORDER_ATTRIBUTE, ordersService.getAllOrderList());
    log.info(ordersService.getAllOrderList() + "sss");
    return "shop/orderList";
  }
}

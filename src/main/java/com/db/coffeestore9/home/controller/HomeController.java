package com.db.coffeestore9.home.controller;

import com.db.coffeestore9.group.service.GroupCardService;
import com.db.coffeestore9.group.service.GroupUserService;
import com.db.coffeestore9.product.domain.Product;
import com.db.coffeestore9.product.service.ProductService;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

  private final ProductService productService;
  private final GroupCardService groupCardService;
  private final GroupUserService groupUserService;

  @GetMapping("/")
  public String home(Model model, Principal principal) {
    List<Product> displayProducts = productService.getProductList();
    model.addAttribute("products", displayProducts);

    return "index";
  }

  @GetMapping("/layouts/header")
  public String header(Model model, Principal principal) {

      if (groupCardService.getGroupCard(principal.getName()) != null) {
        model.addAttribute("groupAdminState", groupUserService.getMyGroupUser(
            principal.getName()).isAdministrator());
      } else {
        model.addAttribute("groupState", null);
      }

    return "layouts/header";
  }
}

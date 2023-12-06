package com.db.coffeestore9.product.controller;

import com.db.coffeestore9.product.common.Category;
import com.db.coffeestore9.product.common.ProductAddForm;
import com.db.coffeestore9.product.common.ProductEditForm;
import com.db.coffeestore9.product.domain.Product;
import com.db.coffeestore9.product.service.ProductService;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @GetMapping("/add-product")
  public String productAddForm() {
    return "/product/addProductForm";
  }

  @PostMapping("/add-product")
  public String processAddProduct(ProductAddForm productAddForm) {
    productService.processingAddProduct(productAddForm);
    return "redirect:/";
  }

  @GetMapping("/single-product/{seq}")
  public String singleProduct(Model model, Principal principal, @PathVariable("seq") Long seq) {
    Product product = productService.findProductBySeq(seq);
    model.addAttribute("product", product);

    return "shop/singleProduct";
  }

  @GetMapping("/search-product")
  public String searchProduct(Model model, Principal principal, String keyword, Category category) {
    List<Product> productList = productService.searchProductList(keyword, category);
    model.addAttribute("products", productList);
    return "index";
  }

  @GetMapping("/product-list")
  public String productList(Model model, Principal principal) {
    model.addAttribute("products", productService.getProductList());
    return "/product/productList";
  }

  @GetMapping("/edit-product/{seq}")
  public String updateProductForm(Model model, Principal principal, @PathVariable("seq") long seq) {
    model.addAttribute("product", productService.findProductBySeq(seq));

    return "/product/editProductForm";
  }

  @PostMapping("/edit-product/{seq}")
  public String updateProduct(Model model, Principal principal, ProductEditForm productEditForm,
      @PathVariable("seq") long seq) {
    productService.updateProduct(seq, productEditForm);
    return "redirect:/product-list";
  }
}

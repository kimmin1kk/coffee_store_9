package com.db.coffeestore9.product.common;

import com.db.coffeestore9.product.domain.Product;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

public record ProductAddForm(
    @NotBlank
    String name,
    @NotBlank
    Integer price,
    @NotBlank
    Category category
) {

  public Product addProduct() {
    return Product.builder()
        .name(name)
        .price(price)
        .category(category)
        .build();
  }

}

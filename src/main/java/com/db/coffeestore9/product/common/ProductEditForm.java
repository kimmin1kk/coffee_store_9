package com.db.coffeestore9.product.common;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

public record ProductEditForm(
    @NotBlank
    String name,
    @NotBlank
    int price,
    @NotBlank
    Category category
) {

}

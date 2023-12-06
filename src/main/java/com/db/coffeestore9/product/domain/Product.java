package com.db.coffeestore9.product.domain;

import com.db.coffeestore9.global.config.BaseTimeEntity;
import com.db.coffeestore9.product.common.Category;
import com.db.coffeestore9.product.common.ProductEditForm;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Product extends BaseTimeEntity {

    @Column(unique = true, nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private Integer price;


    @NotBlank
    @Column(nullable = false)
    private Integer count;


    @NotBlank
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;


    public void updateProduct(ProductEditForm productEditForm) {
        this.name = productEditForm.getName();
        this.price = productEditForm.getPrice();
        this.category = productEditForm.getCategory();
    }

    public void addCount(int count) {
        this.count += count;
    }

    public void minusCount(int count) {
        this.count -= count;
    }

}

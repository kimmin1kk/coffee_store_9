package com.db.coffeestore9.product.service;

import com.db.coffeestore9.product.common.Category;
import com.db.coffeestore9.product.common.ProductAddForm;
import com.db.coffeestore9.product.common.ProductEditForm;
import com.db.coffeestore9.product.domain.Product;
import com.db.coffeestore9.product.repository.ProductRepository;
import com.db.coffeestore9.product.repository.SearchProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final SearchProductRepository searchProductReository;

  public void processingAddProduct(ProductAddForm productAddForm) {
    productRepository.save(productAddForm.addProduct());
  }

  public Product findProductBySeq(Long seq) {
    return productRepository.findById(seq)
        .orElseThrow(() -> new RuntimeException("존재하지 않는 제품입니다."));
  }

  public List<Product> getProductList() {
    return productRepository.findAll();
  }

  public List<Product> searchProductList(String keyword, Category category) {
    if (keyword != null && !keyword.isEmpty() && category != Category.ALL) {
      return searchProductReository.findProductByNameContainingAndCategory(keyword, category);
    } else if (category != Category.ALL) {
      return searchProductReository.findProductByCategory(category);
    } else if (keyword != null) {
      return searchProductReository.findProductByNameContaining(keyword);
    } else {
      return productRepository.findAll();
    }
  }

  @Transactional
  public void updateProduct(Long seq, ProductEditForm productEditForm) {
    Product product = productRepository.findBySeq(seq);
    product.updateProduct(productEditForm);
  }


}
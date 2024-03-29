package com.db.coffeestore9;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class CoffeeStore9Application {

  public static void main(String[] args) {
    SpringApplication.run(CoffeeStore9Application.class, args);
  }

}

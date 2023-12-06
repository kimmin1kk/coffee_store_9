package com.db.coffeestore9.global.config;

import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Aspect
@Component
@RequiredArgsConstructor
public class ControllerAspect {

  @Pointcut("execution(* com.db.coffeestore9.*.controller.*Controller.*(..))")
  public void controllerExcution() {
  }



  @Pointcut("args(model, principal, ..)")
  public void argsWithModelAndPrincipal(Model model, Principal principal) {
  }


  @Before(value = "controllerExcution() && argsWithModelAndPrincipal(model, principal)", argNames = "model, principal")
  public void applySomethingBeforeController(Model model, Principal principal) {
    if (principal != null) {
      model.addAttribute("username", principal.getName());
    }
  }
}

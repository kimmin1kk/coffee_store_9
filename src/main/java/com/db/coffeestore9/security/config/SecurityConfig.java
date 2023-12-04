package com.db.coffeestore9.security.config;


import static org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

  private final CorsFilter corsFilter;

  @Bean
  public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(); //암호화 사용할 경우
    return getInstance(); //암호화 하지 않고 사용할 경우
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

    return httpSecurity
        .formLogin(login -> login
            .loginPage("/login") //User가 로그인 요청할 URL 지정
            .defaultSuccessUrl("/", false)) //사용자가 인증되기 전에 방문하려고 한 페이지로 리다이렉트 됨
        .logout(logout -> logout
            .logoutUrl("/logout") //User가 로그아웃 요청할 URL 지정
            .logoutSuccessUrl("/") //로그아웃 성공 후 사용자가 리다이렉트 될 URL 지정
            .invalidateHttpSession(true) // 로그아웃 성공 시 사용자의 HttpSession 무효화 설정
        )
        .addFilterBefore(corsFilter,
            UsernamePasswordAuthenticationFilter.class) // CORS 필터를 UsernamePassword..필터 전에 추가해서 CORS 관련 처리가 등록된 필터보다 먼저 실행되게 함
        .csrf(
            AbstractHttpConfigurer::disable) //개발할 때 CSRF에 대한 추가 설정 없이 테스트하기 위해 CSRF 보호 비활성화, 실제 배포할 경우에는 활성화 해두는게 좋음
        .sessionManagement(sessionManagement -> {
          sessionManagement.sessionFixation().migrateSession();
          sessionManagement.maximumSessions(1)
              .maxSessionsPreventsLogin(false)
              .expiredUrl("/session-expired");
        })
        .authorizeHttpRequests(registry ->

            registry.requestMatchers("/myPage")
                .hasAnyRole("USER", "ADMIN") //myPage URL 요청 -> 사용자 역할이 USER일 때만 허용됨
                .requestMatchers("/add-card", "/add-address").authenticated()
                .requestMatchers("/", "/**").permitAll()// root 및 모든 경로에 대한 모든 요청은 모든 사용자에게 허용
        )
        .getOrBuild();

  }
}

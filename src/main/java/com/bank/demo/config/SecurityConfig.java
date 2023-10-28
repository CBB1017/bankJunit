package com.bank.demo.config;

import com.bank.demo.domain.account.user.UserEnum;
import com.bank.demo.dto.ResponseDto;
import com.bank.demo.util.CustomResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final Logger log = LoggerFactory.getLogger(getClass());

    //JWT 필터 등록 필요
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        log.debug("디버그 : BCryptPasswordEncoder 빈 등록됨");
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        log.debug("디버그 : configurantion filterchain");
        http.headers().frameOptions().disable(); // iframe 허용 안함
        http.csrf().disable(); // enable이면 포스트맨 작동안함
        http.cors().configurationSource(configurationSource());
        //jSessionId를 서버쪽에서 관리안하겠다.
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.formLogin().disable();
        //httpBasic은 브라우저가 팝업창을 이용해서 사용자 인증을 진행
        http.httpBasic().disable();
        //exceptiom 가로채기
        http.exceptionHandling().authenticationEntryPoint((request, response, authenticationException) -> {
            CustomResponseUtil.unAuthentication(response);
        });
        http.authorizeHttpRequests()
                .antMatchers("/api/s/**").authenticated()
                .antMatchers("/api/admin/**").hasRole(String.valueOf(UserEnum.ADMIN)) // ROLE_를 더이상 안붙여도됨
                .anyRequest().permitAll();
        return http.build();
    }
    public CorsConfigurationSource configurationSource(){
        log.debug("디버그 : Cors 설정이 filterchain에 등록");
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedOriginPattern("*"); // 개발을 위해 모든 주소 허용
        corsConfiguration.setAllowCredentials(true); // 클라이언트 쿠키 요청 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}

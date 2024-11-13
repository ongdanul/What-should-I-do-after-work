package com.elice.boardproject.global.config;

import com.elice.boardproject.user.mapper.UsersMapper;
import com.elice.boardproject.user.service.CustomUserDetailsService;
import com.elice.boardproject.usersAuth.mapper.UsersAuthMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class CustomSecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final AuthenticationConfiguration configuration;
    private final UsersAuthMapper usersAuthMapper;
    private final UsersMapper usersMapper;
    @Bean
    public BCryptPasswordEncoder cryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //예외 처리
        http.exceptionHandling(exceptionHandling -> exceptionHandling
                .accessDeniedHandler(accessDeniedHandler));

        //CSRF 보호 비활성화
        http.csrf(AbstractHttpConfigurer::disable);

        //접근 권한 설정
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/css/**","/fonts/**", "/img/**", "/static/**", "/js/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**", "/login/**", "/check/**", "/error/**").permitAll()
                .requestMatchers("/").permitAll()
                .anyRequest().authenticated())
                //로그인 설정
                .formLogin(form -> form
                            .loginPage("/user/login")
                            .loginProcessingUrl("/user/loginProcess")
                            .failureHandler(customAuthenticationFailureHandler)
                            .permitAll())
                // LoginFilter 추가
                .addFilterAt(new LoginFilter(authenticationManager(configuration),
                                        usersMapper, customAuthenticationFailureHandler),
                                        UsernamePasswordAuthenticationFilter.class)
                // LogoutFilter 추가
                .addFilterBefore(new CustomLogoutFilter(), LogoutFilter.class)
                // 자동 로그인 설정
                .rememberMe(rememberMe -> rememberMe
                                .key("security")
                                .rememberMeParameter("rememberMe")
                                .userDetailsService(userDetailsService)
                                .tokenValiditySeconds(604800));

        //세션 필요한 경우에만 만들어서 사용
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        return http.build();
    }
}

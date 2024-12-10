package com.elice.boardproject.global.config;

import com.elice.boardproject.user.mapper.UsersMapper;
import com.elice.boardproject.user.service.CustomOAuth2UserService;
import com.elice.boardproject.user.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class CustomSecurityConfig {

    private final UsersMapper usersMapper;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final AuthenticationConfiguration configuration;
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOauth2SuccessHandler customOauth2SuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private static final int TOKEN_VALIDITY =  7 * 24 * 60 * 60; //7days

    /**
     * BCryptPasswordEncoder를 Bean으로 등록하여 암호화 기능을 제공합니다.
     *
     * @return BCryptPasswordEncoder 객체
     */
    @Bean
    public BCryptPasswordEncoder cryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager를 Bean으로 등록하여 인증 처리에 사용합니다.
     *
     * @param configuration 인증 설정을 위한 configuration 객체
     * @return AuthenticationManager 객체
     * @throws Exception 인증 매니저 생성 시 예외 발생 가능성 있음
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * HTTP 보안 필터 체인을 설정합니다.
     * 요청에 대한 인증 및 권한 부여, 로그인, 로그아웃, 자동 로그인 및 소셜 로그인을 설정합니다.
     *
     * @param http HTTP 보안 설정 객체
     * @return SecurityFilterChain 객체
     * @throws Exception 보안 필터 체인 설정 시 예외 발생 가능성 있음
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, ClientRegistrationRepository clientRegistrationRepository) throws Exception {
        OAuth2AuthorizationRequestResolver customResolver = new CustomAuthorizationRequestResolver(clientRegistrationRepository);

        //예외 처리
        http.exceptionHandling(exceptionHandling -> exceptionHandling
                .accessDeniedHandler(customAccessDeniedHandler));

        //CSRF 보호 비활성화
        http.csrf(AbstractHttpConfigurer::disable);

        //접근 권한 설정
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/css/**","/fonts/**", "/img/**", "/static/**", "/js/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**", "/login/**", "/check/**", "/oauth2/**", "/error/**").permitAll()
                        .requestMatchers("/").permitAll()
                        .anyRequest().authenticated())
                //로그인 설정
                .formLogin(form -> form
                        .loginPage("/user/login")
                        .loginProcessingUrl("/user/loginProcess")
                        .failureHandler(customAuthenticationFailureHandler)
                        .permitAll())
                //LoginFilter 추가
                .addFilterAt(new CustomLoginFilter(authenticationManager(configuration),
                                usersMapper, customAuthenticationFailureHandler),
                        UsernamePasswordAuthenticationFilter.class)
                //LogoutFilter 추가
                .addFilterBefore(new CustomLogoutFilter(), LogoutFilter.class)
                //자동 로그인 설정
                .rememberMe(rememberMe -> rememberMe
                        .key("security")
                        .rememberMeParameter("rememberMe")
                        .userDetailsService(customUserDetailsService)
                        .tokenValiditySeconds(TOKEN_VALIDITY))
                //소셜 로그인 설정
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2UserService))
                        // 커스텀 Authorization Request Resolver 등록
                        .authorizationEndpoint(endpoint -> endpoint.authorizationRequestResolver(customResolver))
                        .successHandler(customOauth2SuccessHandler)
                        .failureHandler(customAuthenticationFailureHandler));

        //세션 필요한 경우에만 만들어서 사용
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        return http.build();
    }
}

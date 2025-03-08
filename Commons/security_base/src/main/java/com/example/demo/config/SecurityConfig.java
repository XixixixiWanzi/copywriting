package com.example.demo.config;

import com.example.demo.Filters.JwtAuthenticationFilter;
import com.example.demo.Filters.CustomAuthorityFilter;
import com.example.demo.UserDetailService.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.security.web.session.DisableEncodeUrlFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class SecurityConfig {
    private CustomUserDetailsService customUserDetailsService;
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private CustomAuthorityFilter authorityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http .authorizeHttpRequests((authorize) -> {
            // 请求过滤管理
            authorize.requestMatchers(new AntPathRequestMatcher("/ucenter/forPassword/**")).permitAll();
            authorize.requestMatchers(new AntPathRequestMatcher("/ucenter/forLogin/**")).permitAll();
            authorize.requestMatchers(new AntPathRequestMatcher("/ucenter/forRegister/**")).permitAll();
            authorize.requestMatchers(new AntPathRequestMatcher("/aliService/msm/**")).permitAll();
            authorize.requestMatchers(new AntPathRequestMatcher("/aliService/oss/**")).permitAll();
            authorize.requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll();
            authorize.requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**")).permitAll();
//          authorize.anyRequest().hasAuthority("ROLE_ANONYMOUS");   启用 无认证 访问
            authorize.anyRequest().access((authentication, object) -> {
                return authorityFilter.authorizationDecision(authentication, object);
            });
        })
        .csrf(csrf-> csrf.disable())    //  禁用csrf
                .cors((cors) -> cors
                        .configurationSource(apiConfigurationSource()))
        .addFilterBefore(jwtAuthenticationFilter, AuthorizationFilter.class)  // 修改filter顺序
        .sessionManagement((session) -> session  //禁用对话认证
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        return http.build();
    }


    CorsConfigurationSource apiConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
/*草你妈，一行代码搞得我崩溃，就6个字母的差距   官方文档都没给这行代码，搞得我夜不能寐。必须留下标记。学习开发以来，就没有受过这样大的委屈。
 *      ┌─┐       ┌─┐
 *   ┌──┘ ┴───────┘ ┴──┐
 *   │                 │
 *   │       ───       │
 *   │   >        <    │
 *   │                 │
 *   │   ...  ⌒  ...   │
 *   │                 │
 *   └───┐         ┌───┘
 *       │         │
 *       │         │
 *       │         │
 *       │         └──────────────┐
 *       │                        │
 *       │                        ├─┐
 *       │                        ┌─┘
 *       │                        │
 *       └─┐  ┐  ┌───────┬──┐  ┌──┘
 *         │ ─┤ ─┤       │ ─┤ ─┤
 *         └──┴──┘       └──┴──┘
*/
        configuration.setAllowedHeaders(Arrays.asList("*"));


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }






//    @Bean
//    @Order(1)
//    public SecurityFilterChain myOtherFilterChain(HttpSecurity http) throws Exception {
//        http
//        .cors((cors) -> cors
//                .configurationSource(myWebsiteConfigurationSource())
//        );
//
//        return http.build();
//    }
//    CorsConfigurationSource myWebsiteConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("https://example.com"));
//        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }





}


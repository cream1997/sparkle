package com.cream.sparkle.config.security;

import com.cream.sparkle.config.security.sub.JwtAuthenticationFilter;
import com.cream.sparkle.config.security.sub.QuanXianEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        //开启授权防护
        httpSecurity
                .authorizeHttpRequests(this::authorizeConfig)
                /* 设置无状态会话：STATELESS表示服务器不会创建和维护用户的会话状态。每个请求都必须携带
                完整的认证信息（如JWT Token），服务器不会使用Session ID来跟踪用户状态 */
                .sessionManagement(
                        session ->
                                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 添加jwt过滤器
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                // todo 解决跨域问题？ 待测试
                .cors(Customizer.withDefaults())
                // todo 关闭csrf攻击防御, 有什么作用？不写会怎样？
                .csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

    /**
     * 授权配置
     */
    private void authorizeConfig(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize) {
        authorize
                // 放开的路径
                .requestMatchers(
                        "/test",
                        "/downloadLatestVersion",
                        "/latestVersionNumber"
                )
                .permitAll()
                // 配置/hero/**路径下的权限
                .requestMatchers("/hero/login", "/hero/register").permitAll()
                .requestMatchers("/hero/**").hasAuthority(QuanXianEnum.HERO.name())
                // 对其他请求开启授权保护，已认证的请求会被自动授权
                .anyRequest()
                .authenticated();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
}

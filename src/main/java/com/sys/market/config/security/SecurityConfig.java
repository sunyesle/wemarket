package com.sys.market.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/users/signIn", "/api/users/refresh", "/main", "/exception/**").permitAll()
                .antMatchers("/api/users/verify").hasRole("UNVERIFIED")

                .antMatchers(HttpMethod.POST, "/api/users/**").hasRole("USER")
                .antMatchers(HttpMethod.PUT, "/api/users/**").hasRole("USER")
                .antMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("USER")

                .antMatchers(HttpMethod.POST, "/api/items/**").hasRole("USER")
                .antMatchers(HttpMethod.PUT, "/api/items/**").hasRole("USER")
                .antMatchers(HttpMethod.DELETE, "/api/items/**").hasRole("USER")

                .antMatchers(HttpMethod.POST, "/api/offers/**").hasRole("USER")
                .antMatchers(HttpMethod.PUT, "/api/offers/**").hasRole("USER")
                .antMatchers(HttpMethod.DELETE, "/api/offers/**").hasRole("USER")

                .antMatchers(HttpMethod.POST, "/api/reviews/**").hasRole("USER")
                .antMatchers(HttpMethod.PUT, "/api/reviews/**").hasRole("USER")
                .antMatchers(HttpMethod.DELETE, "/api/reviews/**").hasRole("USER")

                .antMatchers(HttpMethod.POST, "/api/wishlists/**").hasRole("USER")
                .antMatchers(HttpMethod.DELETE, "/api/wishlists/**").hasRole("USER")

                .antMatchers("/api/chats/**").hasRole("USER")
                .antMatchers("/api/messages/**").hasRole("USER")
                .anyRequest().permitAll()
                .and()
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web){
        web.ignoring().antMatchers("/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-ui.html",
                "/swagger-ui/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}

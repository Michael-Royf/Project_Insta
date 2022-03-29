package com.example.security;

import com.example.security.v2.CustomAuthorizationFilter1;
import com.example.security.v2.JwtAccessDeniedHandler;
import com.example.security.v2.JwtAuthenticationEntryPoint111;
import com.example.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import  static  com.example.constant.SecurityConstant.*;


@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, proxyTargetClass = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    JwtAuthenticationEntryPoint111 jwtAuthenticationEntryPoint111;
    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @Autowired
    private CustomAuthorizationFilter1 customAuthorizationFilter1;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
                .antMatchers("/resources/**", "/swagger*/**").permitAll()
                //.antMatchers("api/auth/home/**").permitAll()
                .antMatchers("api/auth/login/**").permitAll()
                .antMatchers(PUBLIC_SWAGGER).permitAll()
                .mvcMatchers("/api/auth/login/**", "/api/auth/token/refresh/**", "/api/auth/signup", "/api/auth/confirm/**").permitAll()
                .anyRequest().authenticated();
        http.addFilterBefore(customAuthorizationFilter1, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling().accessDeniedHandler(jwtAccessDeniedHandler).authenticationEntryPoint(jwtAuthenticationEntryPoint111);


    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
//  CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
// customAuthenticationFilter.setFilterProcessesUrl("/api/auth/login");
//  http.addFilter(customAuthenticationFilter);
//   http.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);

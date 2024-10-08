package com.example.bookstorebackend.security.config;


import com.example.bookstorebackend.security.CustomAccessDeniedHandler;
import com.example.bookstorebackend.security.RestAuthenticationEntryPoint;
import com.example.bookstorebackend.security.jwt.JwtAuthenticationFilter;
import com.example.bookstorebackend.service.extend.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AccountService accountService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public RestAuthenticationEntryPoint restServicesEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService).passwordEncoder(new BCryptPasswordEncoder(10));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().ignoringAntMatchers("/api/**");
        http.httpBasic().authenticationEntryPoint(restServicesEntryPoint());
        http.authorizeRequests()
                .antMatchers( "/api/**").permitAll()
                .antMatchers( "/api/find-all-account").hasAnyRole(new String[]{"ADMIN", "USER"})
                .anyRequest().authenticated()
                .and().csrf().disable();
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler());
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.cors();
    }
}
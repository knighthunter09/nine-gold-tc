package com.ninegold.ninegoldapi.security;

import com.ninegold.ninegoldapi.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.springframework.http.HttpMethod.*;

/**
 * The application security config.
 */
@Configuration
@EnableWebSecurity
@Order(1)
@EnableGlobalMethodSecurity(securedEnabled = true, proxyTargetClass = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * The user detail service.
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * The password encoder.
     *
     * @return password encoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return Helper.getPasswordEncoder();
    }

    /**
     * Configure global auth manager builder.
     *
     * @param auth the auth manager builder
     * @throws Exception throws if any error happen
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(Helper.getPasswordEncoder());
        authProvider.setUserDetailsService(userDetailsService);
        auth.authenticationProvider(authProvider);
        auth.userDetailsService(userDetailsService);
    }

    /**
     * Create auth manager bean.
     *
     * @return the auth manager bean.
     * @throws Exception throws if any error happen
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    /**
     * Configure authentication.
     *
     * @param http the http
     * @throws Exception if there is any error
     */
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationEntryPoint entryPoint = new CustomAuthenticationEntryPoint();
        http.csrf()
            .disable()
            .authorizeRequests()
            .antMatchers("/").permitAll()
                //allow anonymous for lookup,forgot password, update password requests
                .antMatchers("/favicon.ico")
                .permitAll()
                .antMatchers("/")
                .permitAll()
                .antMatchers(GET,"/resources/**", "/assets/*", "/assets/img/*", "/*.js", "*/img/*", "/login", "/register", "/forgot-password", "/faq", "/profile")
                .permitAll()
                .antMatchers(GET, "/images/{imageUrl}")
                .permitAll()
                .antMatchers(GET, "/v2/api-docs")
                .permitAll()
                .antMatchers(POST, "/auth/login")
                .hasAuthority("ROLE_USER")
                .antMatchers(GET, "/auth/logout")
                .hasAuthority("ROLE_USER")
                .antMatchers("/stripe/plan")
                .permitAll()
                .antMatchers("/stripe/handleEvent")
                .permitAll()
                .antMatchers("/users/forgotPassword")
                .permitAll()
                .antMatchers("/users/updatePassword")
                .permitAll()
                .antMatchers(GET, "/users")
                .hasAuthority("ROLE_USER")
                .antMatchers(GET, "/users/me")
                .hasAuthority("ROLE_USER")
                .antMatchers(POST, "/users/{planId}")
                .permitAll()
                .antMatchers(DELETE, "/users/terminate")
                .hasAuthority("ROLE_USER")
                .antMatchers(PUT, "/users/updatePayment")
                .hasAuthority("ROLE_USER")
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic().authenticationEntryPoint(entryPoint);
    }
}


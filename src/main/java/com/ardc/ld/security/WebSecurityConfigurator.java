package com.ardc.ld.security;

import com.ardc.ld.config.CorsFilter;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

@Configuration(enforceUniqueMethods = false)
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfigurator {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler customAuthenticationFailureHandler;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public WebSecurityCustomizer web() {
        return (web) -> web.ignoring().requestMatchers("/custom/**", "/lib/**");
    }

    @Bean
    @Order(1)
    public SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.securityMatcher("/**")
                .authorizeHttpRequests()
                .requestMatchers("/api/**/public/**", "/login**", "/logout**")
                .permitAll() // Public
                .anyRequest().authenticated() // Everything else is Authenticated
                .and()
                .httpBasic()
                .and()
                .formLogin()
                .successHandler(customAuthenticationSuccessHandler)
                .failureHandler(customAuthenticationFailureHandler)
                .loginProcessingUrl("/")
                .defaultSuccessUrl("/swagger-ui.html", true)
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login")
                .permitAll()
                .and()
                .exceptionHandling()
                .and()
                .headers()
                .frameOptions()
                .disable()
                .and()
                .csrf()
                .csrfTokenRepository(csrfTokenRepository())
                .ignoringRequestMatchers("/login**")
                .ignoringRequestMatchers("/logout**")
                .ignoringRequestMatchers("/api/**")
                .ignoringRequestMatchers("**/delete/**")
                .ignoringRequestMatchers("/h2-console/**")
                .and()
                .sessionManagement()
                .sessionFixation()
                .newSession()
                .and()
                .addFilterBefore(new CorsFilter(), CsrfFilter.class)
                .addFilterAfter(csrfHeaderFilter(), CsrfFilter.class)
                .addFilterBefore(new UsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(authProvider());
    }

    private Filter csrfHeaderFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain) throws ServletException, IOException {
                CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

                if (csrf != null) {
                    Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
                    String token = csrf.getToken();
                    if (cookie == null || token != null && !token.equals(cookie.getValue())) {
                        cookie = new Cookie("XSRF-TOKEN", token);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                    }
                }
                try {
                    filterChain.doFilter(request, response);
                } catch (Exception e) {
                    logger.error(String.format("Error while processing request: %s", e.getMessage()));
                }
            }
        };
    }

    @Bean
    public JsonEncodedPasswordAuthenticationFilter formJsonFilter(AuthenticationManager authenticationManager) {
        JsonEncodedPasswordAuthenticationFilter filter = new JsonEncodedPasswordAuthenticationFilter();
        filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
        filter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);
        filter.setAuthenticationManager(authenticationManager);
        filter.setUsernameParameter("username");
        filter.setPasswordParameter("password");
        return filter;
    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }
}

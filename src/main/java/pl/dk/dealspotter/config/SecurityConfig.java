package pl.dk.dealspotter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request -> request
                .requestMatchers("/styles/**").permitAll()
                .requestMatchers("/img/**").permitAll()
                .requestMatchers("/").permitAll()
                .requestMatchers("/admin/**")
                .hasRole("ADMIN")
                .requestMatchers("/user/**", "/promo/save/**", "/promo/edit/**", "/promo/update/**", "/promo/delete/*")
                .hasAnyRole("USER", "ADMIN")
                .anyRequest()
                .permitAll());

        httpSecurity.formLogin(login -> login.loginPage("/login").permitAll());
        httpSecurity.logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout/**", HttpMethod.GET.name()))
                .logoutSuccessUrl("/")

        );
        httpSecurity.headers().frameOptions().sameOrigin();

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}

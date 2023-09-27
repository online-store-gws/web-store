package uz.greenwhite.webstore.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import uz.greenwhite.webstore.enums.UserRole;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                // Spring Security should completely ignore URLs starting with /resources/
                .requestMatchers("/resources/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(c -> c.disable());
        http
                .securityMatcher("/**")
                .authorizeHttpRequests(
                        authorizationManagerRequestMatcherRegistry ->
                                authorizationManagerRequestMatcherRegistry
                                        .requestMatchers(
                                                PathRequest
                                                        .toStaticResources()
                                                        .atCommonLocations())
                                        .permitAll()
                )
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/")
                        .permitAll()
                        .requestMatchers("/moderator/**").hasRole(UserRole.MODERATOR.name())
                        .requestMatchers("/seller/**").hasAnyRole(UserRole.SELLER.name(), UserRole.MODERATOR.name())
                        .requestMatchers("/login", "/register").permitAll()

                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler((request, response, authentication) -> {
                            if (authentication.getPrincipal() instanceof User user) {
                                if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + UserRole.MODERATOR))) {
                                    response.sendRedirect("moderator/dashboard");
                                }
                            }
                            else if (authentication.getPrincipal() instanceof User user) {
                                if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + UserRole.SELLER))) {
                                    response.sendRedirect("seller/dashboard");
                                }
                            }
                            else
                                response.sendRedirect("/");
                        })
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll());

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}

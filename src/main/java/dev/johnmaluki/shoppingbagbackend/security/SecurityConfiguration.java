package dev.johnmaluki.shoppingbagbackend.security;

import dev.johnmaluki.shoppingbagbackend.repository.UserRepository;
import dev.johnmaluki.shoppingbagbackend.security.filter.jwt.JwtAuthenticationFilter;
import dev.johnmaluki.shoppingbagbackend.security.filter.jwt.JwtAuthorizationFilter;
import dev.johnmaluki.shoppingbagbackend.security.user.UserRoles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final UserPrincipalDetailService userPrincipalDetailService;
    private final UserRepository userRepository;

    public SecurityConfiguration(UserPrincipalDetailService userPrincipalDetailService,
                                 UserRepository userRepository) {
        this.userPrincipalDetailService = userPrincipalDetailService;
        this.userRepository = userRepository;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
        jwtAuthenticationFilter.setFilterProcessesUrl("/v1/login");
        http
                .cors()
                .configurationSource(corsConfigurationSource())
                .and()
                // remove csrf and state because in jwt we do not need them
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //Add Jwt filters (1. Authentication, 2. Authorization)
                .addFilter(jwtAuthenticationFilter)
                .addFilter(new JwtAuthorizationFilter(this.userRepository, authenticationManager()))
                .authorizeRequests()
                // Configuring the access rules
                .antMatchers("/v1/login", "/v1/user/register").permitAll()
                .antMatchers("/v1/shopping_bag/**/").hasRole(UserRoles.USER);
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(this.getPasswordEncoder());
        daoAuthenticationProvider
                .setUserDetailsService(this.userPrincipalDetailService);
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PATCH"));
        configuration.applyPermitDefaultValues();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

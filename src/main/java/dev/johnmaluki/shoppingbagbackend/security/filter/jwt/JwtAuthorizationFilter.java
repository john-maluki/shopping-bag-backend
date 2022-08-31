package dev.johnmaluki.shoppingbagbackend.security.filter.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.johnmaluki.shoppingbagbackend.entity.User;
import dev.johnmaluki.shoppingbagbackend.exception.NotFoundException;
import dev.johnmaluki.shoppingbagbackend.repository.UserRepository;
import dev.johnmaluki.shoppingbagbackend.security.JwtProperties;
import dev.johnmaluki.shoppingbagbackend.security.JwtUserTokenUtil;
import dev.johnmaluki.shoppingbagbackend.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final UserRepository userRepository;

    public JwtAuthorizationFilter(UserRepository userRepository, AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //Read the Authorization header, where the JWT token should be
        String header = request.getHeader(JwtProperties.HEADER_STRING);

        // If header does not contain bearer or is null, delegate to Spring impl and exit
        if(header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        } else {
            this.authenticateRequest(request, response);
        }
        chain.doFilter(request, response);
    }

    private void authenticateRequest(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        try {
            String header = request.getHeader(JwtProperties.HEADER_STRING);
            String token = header.substring(JwtProperties.TOKEN_PREFIX.length());
            String username = this.obtainUsername(token);

            this.setUserSecurityContext(username);
        } catch (Exception ex){
            log.error("Error logging in: {}", ex.getMessage());
            this.tokenException(response, ex);
        }
    }

    private void setUserSecurityContext(String username) {
        UserPrincipal userPrincipal = this.getUserByUsername(username);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                username, null, userPrincipal.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private void tokenException(HttpServletResponse response, Exception ex) throws IOException {
        response.setStatus(FORBIDDEN.value());
        Map<String, String> error = new HashMap<>();
        error.put("error_message", ex.getMessage());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }

    private String obtainUsername(String token) {
        JwtUserTokenUtil jwtUserTokenUtil = new JwtUserTokenUtil();
        return jwtUserTokenUtil.getSubjectFromJwtToken(token);
    }

    private UserPrincipal getUserByUsername(String username){
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty()) {
            throw new NotFoundException("User not found!");
        }
        return new UserPrincipal(optionalUser.get());
    }


}

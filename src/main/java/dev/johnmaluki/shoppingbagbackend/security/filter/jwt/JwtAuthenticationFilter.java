package dev.johnmaluki.shoppingbagbackend.security.filter.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.johnmaluki.shoppingbagbackend.Dto.LoginDto;
import dev.johnmaluki.shoppingbagbackend.security.JwtProperties;
import dev.johnmaluki.shoppingbagbackend.security.JwtUserTokenUtil;
import dev.johnmaluki.shoppingbagbackend.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    //Trigger when we issue POST request to /login
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //Grab credentials and map them to LoginPdo
        LoginDto credentials;
        try {
            credentials = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginDto.class);
            log.info("Trying to log user with Username: {} and Password: {}",
                    credentials.getUsername(), credentials.getPassword());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Create login token
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                credentials.getUsername(),
                credentials.getPassword(),
                Collections.emptyList()
        );

        //Authenticate user
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain, Authentication authResult) throws IOException {
        //Grab principal
        UserPrincipal principal = (UserPrincipal) authResult.getPrincipal();

        //Create JWT Tokens
        JwtUserTokenUtil jwtUserTokenUtil = new JwtUserTokenUtil(principal, request.getRequestURL().toString());
        String accessToken = jwtUserTokenUtil.obtainUserAccessToken();
        String refreshToken = jwtUserTokenUtil.obtainUserRefreshToken();

        //Add token in the response
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessToken);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
}

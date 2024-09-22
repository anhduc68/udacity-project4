package com.example.demo.sercuirty;

import com.auth0.jwt.JWT;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JWTAuthenticationVerFilter extends BasicAuthenticationFilter {

    public JWTAuthenticationVerFilter(AuthenticationManager manager) {
        super(manager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        // get header
        String header = req.getHeader(SecurityConstants.HEADER_STRING);
        //check if header is null
        if (header == null ||
                !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        // get Username password
        UsernamePasswordAuthenticationToken authentication
                = getAuthentication(req);

        // do Filter
        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {
        //get Token
        String token
                = req.getHeader(SecurityConstants.HEADER_STRING);

        // if Token is null
        if (token != null) {
            String user = JWT
                    .require(HMAC512(SecurityConstants.SECRET.getBytes())).build()
                    .verify(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
                    .getSubject();

        // if User is null
            if (user != null) {
                return
                        new UsernamePasswordAuthenticationToken(user,
                                null,
                                new ArrayList<>());
            }
            return null;
        }
        return null;
    }
}



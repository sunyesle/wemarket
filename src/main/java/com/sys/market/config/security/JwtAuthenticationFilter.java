package com.sys.market.config.security;

import com.sys.market.entity.User;
import com.sys.market.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtil cookieUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("request: " + request.getRequestURI());
        // cookie에 저장해둔 jwt 토큰 값을 통해 인증을 확인한다.

        Cookie tokenCookie = cookieUtil.getCookie(request, JwtTokenProvider.ACCESS_TOKEN_NAME);
        if(tokenCookie != null) {
            String accessToken = tokenCookie.getValue();

            // accessToken 검증
            if(accessToken != null && jwtTokenProvider.validateToken(accessToken)){
                // token에 담긴 값을 사용해 Authentication 객체 생성한다.
                Authentication auth = jwtTokenProvider.getAuthentication(accessToken);
                // 유저 정보를 가진 Authentication 객체를 SecurityContextHolder에 저장한다.
                SecurityContextHolder.getContext().setAuthentication(auth);

                filterChain.doFilter(request, response);
                return;
            }
        }

        // accessToken이 존재하지 않거나 유효하지 않을경우
        Cookie refreshTokenCookie = cookieUtil.getCookie(request, JwtTokenProvider.REFRESH_TOKEN_NAME);
        if(refreshTokenCookie != null) {
            String refreshToken = refreshTokenCookie.getValue();

            // refreshToken 검증
            if(refreshToken != null && jwtTokenProvider.validateRefreshToken(refreshToken)){
                // 새로운 accessToken 발급
                User user = (User) jwtTokenProvider.getAuthenticationFromRefreshToken(refreshToken).getPrincipal();
                String newAccessToken = jwtTokenProvider.createAccessToken(user);

                Authentication auth = jwtTokenProvider.getAuthentication(newAccessToken);
                SecurityContextHolder.getContext().setAuthentication(auth);

                Cookie accessTokenCookie = cookieUtil.createCookie(JwtTokenProvider.ACCESS_TOKEN_NAME, newAccessToken, JwtTokenProvider.ACCESS_TOKEN_VALID_MILLISECOND / 1000);
                response.addCookie(accessTokenCookie);
            }
        }
        filterChain.doFilter(request, response);
    }
}

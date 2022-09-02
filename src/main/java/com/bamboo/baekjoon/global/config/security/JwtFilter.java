package com.bamboo.baekjoon.global.config.security;

import com.bamboo.baekjoon.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final TokenService tokenService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();

        if (StringUtils.hasText(jwt) && tokenService.validateToken(jwt)) {
            User user = tokenService.getUserByToken(jwt);
            Authentication authentication = getAuthentication(user);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User authUser = (User) authentication.getPrincipal();
            String authUserString = "id=" + authUser.getId() + ", korName=" + authUser.getKorName() + ", username=" + authUser.getUsername();

            log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authUserString, requestURI);
        } else {
            log.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Authentication getAuthentication(User user) {
        return new UsernamePasswordAuthenticationToken(user, "", Arrays.asList(new SimpleGrantedAuthority(user.getRole().getKey())));
    }
}
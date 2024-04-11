package com.supportrip.core.auth.filter;

import com.supportrip.core.auth.domain.OidcUser;
import com.supportrip.core.auth.dto.AuthPayload;
import com.supportrip.core.auth.jwt.JwtProvider;
import com.supportrip.core.auth.kakao.OidcKakaoAuthenticationToken;
import com.supportrip.core.auth.jwt.JwtUtil;
import com.supportrip.core.user.domain.User;
import com.supportrip.core.user.exception.UserNotFoundException;
import com.supportrip.core.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authorizationHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
        String token = JwtUtil.extractTokenWithoutThrow(authorizationHeader);

        if (StringUtils.hasText(token) && jwtProvider.validateToken(token)) {
            AuthPayload authPayload = jwtProvider.parseAccessToken(token);
            log.info("User[ID={}] has been authenticated", authPayload.getUserId());
            Authentication authentication = getAuthentication(authPayload);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

    private Authentication getAuthentication(AuthPayload authPayload) {
        Long userId = authPayload.getUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(LogLevel.ERROR));
        OidcUser oidcUser = OidcUser.from(user);
        return new OidcKakaoAuthenticationToken(oidcUser);
    }
}

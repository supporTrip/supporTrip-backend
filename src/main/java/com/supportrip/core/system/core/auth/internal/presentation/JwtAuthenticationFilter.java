package com.supportrip.core.system.core.auth.internal.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supportrip.core.system.core.auth.internal.domain.OidcUser;
import com.supportrip.core.system.core.auth.internal.domain.AuthPayload;
import com.supportrip.core.context.error.exception.unauthorized.DisabledOrLockedAuthenticationException;
import com.supportrip.core.system.core.auth.internal.application.JwtProvider;
import com.supportrip.core.system.core.auth.internal.application.JwtUtil;
import com.supportrip.core.context.error.exception.unauthorized.ExpiredTokenException;
import com.supportrip.core.system.core.auth.internal.domain.OidcKakaoAuthenticationToken;
import com.supportrip.core.context.error.ErrorInfo;
import com.supportrip.core.context.error.ErrorResponse;
import com.supportrip.core.context.error.exception.BusinessException;
import com.supportrip.core.system.core.user.internal.domain.User;
import com.supportrip.core.context.error.exception.notfound.UserNotFoundException;
import com.supportrip.core.system.core.user.internal.domain.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
    private final ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authorizationHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
        String token = JwtUtil.extractTokenWithoutThrow(authorizationHeader);

        try {
            if (StringUtils.hasText(token) && jwtProvider.validateToken(token)) {
                AuthPayload authPayload = jwtProvider.parseAccessToken(token);
                log.info("User[ID={}] has been authenticated", authPayload.getUserId());
                Authentication authentication = getAuthentication(authPayload);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredTokenException exception) {
            sendError((HttpServletResponse) response, exception);
            return;
        } catch (DisabledOrLockedAuthenticationException exception) {
            log.info("Login Fail Exception: ", exception);
            sendError((HttpServletResponse) response, exception);
            return;
        }

        chain.doFilter(request, response);
    }

    private Authentication getAuthentication(AuthPayload authPayload) {
        Long userId = authPayload.getUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(LogLevel.ERROR));

        if (!user.isEnabled() || user.isLocked()) {
            throw new DisabledOrLockedAuthenticationException();
        }

        OidcUser oidcUser = OidcUser.from(user);
        return new OidcKakaoAuthenticationToken(oidcUser);
    }

    private void sendError(HttpServletResponse response, BusinessException exception) throws IOException {
        ErrorInfo errorInfo = exception.getErrorInfo();
        ErrorResponse errorResponse = ErrorResponse.from(errorInfo);

        byte[] responseBody = objectMapper.writeValueAsBytes(errorResponse);
        response.setStatus(errorInfo.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(responseBody);
    }
}

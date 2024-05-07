package com.supportrip.core.system.core.auth.internal.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supportrip.core.context.error.ErrorInfo;
import com.supportrip.core.context.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@RequiredArgsConstructor
public class Http401AuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        ErrorInfo errorInfo = ErrorInfo.UNAUTHORIZED_REQUEST;
        ErrorResponse errorResponse = ErrorResponse.from(errorInfo);

        byte[] responseBody = objectMapper.writeValueAsBytes(errorResponse);
        response.setStatus(errorInfo.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(responseBody);
    }
}

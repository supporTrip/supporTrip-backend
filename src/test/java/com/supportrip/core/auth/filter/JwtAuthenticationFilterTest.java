package com.supportrip.core.auth.filter;

import com.supportrip.core.auth.dto.AuthPayload;
import com.supportrip.core.auth.jwt.JwtProvider;
import com.supportrip.core.user.domain.User;
import com.supportrip.core.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("인증된 유저의 JWT로 요청한 경우 SecurityContextHolder에 Authentication을 저장한다.")
    void doFilterByAuthenticatedUser() throws Exception {
        final String VALID_AUTHORIZATION_HEADER = "Bearer valid_token";
        final Long USER_ID = 1L;

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        given(request.getHeader(anyString())).willReturn(VALID_AUTHORIZATION_HEADER);
        given(jwtProvider.validateToken(anyString())).willReturn(true);
        given(jwtProvider.parseAccessToken(anyString())).willReturn(AuthPayload.from(USER_ID));
        given(userRepository.findById(anyLong())).willReturn(Optional.of(User.initialUserOf(null)));

        // when
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        // then
        verify(jwtProvider).parseAccessToken(anyString());
        verify(userRepository).findById(anyLong());
    }

    @Test
    @DisplayName("Authorization Header 없이 요청한 경우 다음 Filter로 넘어간다.")
    void doFilterWithoutAuthorizationHeader() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        given(request.getHeader(anyString())).willReturn(null);

        // when
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        // then
        verify(jwtProvider, never()).parseAccessToken(anyString());
    }

    @Test
    @DisplayName("미인증 유저의 JWT로 요청한 경우 다음 Filter로 넘어간다.")
    void doFilterByUnauthenticatedUser() throws Exception {
        // given
        final String INVALID_AUTHORIZATION_HEADER = "Bearer invalid_token";

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        given(request.getHeader(anyString())).willReturn(INVALID_AUTHORIZATION_HEADER);
        given(jwtProvider.validateToken(anyString())).willReturn(false);

        // when
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        // then
        verify(jwtProvider, never()).parseAccessToken(anyString());
    }
}
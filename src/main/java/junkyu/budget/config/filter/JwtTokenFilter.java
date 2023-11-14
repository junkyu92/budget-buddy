package junkyu.budget.config.filter;

import junkyu.budget.config.provider.JwtTokenProvider;
import junkyu.budget.exception.CustomException;
import junkyu.budget.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private static final String[] ALL_WHITELIST ={
            "/api/v1/users/signin"
            , "/api/v1/users/signup"
            , "/api/v1/users/token"
            , "/swagger-ui/**"
            , "/swagger-resources/**"
            , "/swagger-resources"
            , "/v2/api-docs"};

    private boolean isFilterCheck(String requestURI) {
        return !PatternMatchUtils.simpleMatch(ALL_WHITELIST, requestURI);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        if(!StringUtils.hasText(request.getHeader(HttpHeaders.AUTHORIZATION))){
            throw new CustomException(ErrorCode.MISSING_ACCESS_TOKEN);
        }
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!bearerToken.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.MISSING_ACCESS_TOKEN);
        }
        return bearerToken.substring(7);
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException {
        try {
            if (isFilterCheck(request.getRequestURI())) {
                String token = getTokenFromRequest(request);
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(jwtTokenProvider.getAuthentication(token));
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }

    }
}

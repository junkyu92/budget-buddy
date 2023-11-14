package junkyu.budget.config.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import junkyu.budget.dto.TokenIssuanceDto;
import junkyu.budget.enums.UserRole;
import junkyu.budget.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final UserDetailsServiceImpl userDetailsService;
    private final StringRedisTemplate stringRedisTemplate;

    @Value("${token.key}")
    private String issuer;
    private SecretKey secretKey;

    /* SecretKey 초기화 메서드 (빈 생성 시 1회 실행) */
    @PostConstruct
    public void init() {
        secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    /* Access Token 생성 메서드  */
    public String generateAccessToken(TokenIssuanceDto tokenTokenIssuanceDTO) {
        Claims claims = Jwts.claims().setSubject(tokenTokenIssuanceDTO.getId().toString());
        claims.put("account", tokenTokenIssuanceDTO.getAccount());
        claims.put("userRole", tokenTokenIssuanceDTO.getUserRole().getRoleName());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(issuer)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // 유효 기간 1시간
                .setExpiration(new Date(System.currentTimeMillis() + (60 * 60 * 1000)))
                .signWith(secretKey)
                .compact();
    }

    /* Refresh Token 생성 메서드*/
    public String generateRefreshToken(String account) {
        String refreshToken = Jwts.builder()
                .claim("account", account)
                .setIssuer(issuer)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // 유효 기간 24시간
                .setExpiration(new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)))
                .signWith(secretKey)
                .compact();

        //레디스에 저장
        String redisKey = "refreshToken:" + account;
        if(stringRedisTemplate.opsForValue().get(redisKey)!=null){
            stringRedisTemplate.delete(redisKey);
        }
        stringRedisTemplate.opsForValue().set(redisKey, refreshToken);

        return refreshToken;
    }

    /* 토큰 유효성 검증 */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException ex) {
            return false;
        }
    }

    /* 토큰에서 Id 추출 */
    public Long getIdFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return Long.parseLong(Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }

    /* 토큰에서 계정 추출 */
    public String getAccountFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("account", String.class);
    }

    /* 토큰 인증 */
    public Authentication getAuthentication(String token) {

        // 토큰을 파싱하여 클레임 추출
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // 클레임에서 계정과 사용자 역할 가져오기
        String account = claims.get("account", String.class);
        UserRole userRole = UserRole.valueOf(claims.get("userRole", String.class));

        // 사용자 정보 로드
        UserDetails userDetails = userDetailsService.loadUserByUsername(account);

        // 사용자 권한과 역할 권한을 병합하여 Authentication 객체 생성
        List<GrantedAuthority> authorities = new ArrayList<>(userDetails.getAuthorities());
        authorities.addAll(userRole.getAuthorities());

        return new UsernamePasswordAuthenticationToken(
                userDetails, "", authorities
        );
    }
}

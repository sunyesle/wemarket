package com.sys.market.config.security;

import com.sys.market.dto.TokenSet;
import com.sys.market.entity.User;
import com.sys.market.repository.RefreshTokenRepository;
import com.sys.market.repository.SignOutRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

// jwt 관련 메소드들을 정의
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final RefreshTokenRepository refreshTokenRepository;
    private final SignOutRepository signOutRepository;

    @Value("spring.jwt.access_token_secret")
    private String accessSecreteKey;
    @Value("spring.jwt.refresh_token_secret")
    private String refreshSecretKey;

    public static final long ACCESS_TOKEN_VALID_MILLISECOND = 1000L * 60 * 60; // 1시간
    public static final long REFRESH_TOKEN_VALID_MILLISECOND = 1000L * 60 * 60 * 24; // 24시간

    public static final String ACCESS_TOKEN_NAME = "accessToken";
    public static final String REFRESH_TOKEN_NAME = "refreshToken";

    public static final String CLAIM_KEY_ROLES = "roles";

    // access 토큰 생성
    public String createAccessToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getId());
        claims.put(CLAIM_KEY_ROLES, user.getRole());
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_MILLISECOND))
                .signWith(SignatureAlgorithm.HS256, accessSecreteKey)
                .compact();
    }

    // access, refresh 토큰 생성
    public TokenSet createTokens(User user){
        TokenSet tokenSet = new TokenSet();

        Claims claims = Jwts.claims().setSubject(user.getId());
        claims.put(CLAIM_KEY_ROLES, user.getRole());
        Date now = new Date();

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_MILLISECOND))
                .signWith(SignatureAlgorithm.HS256, accessSecreteKey)
                .compact();
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_VALID_MILLISECOND))
                .signWith(SignatureAlgorithm.HS256, refreshSecretKey)
                .compact();

        tokenSet.setAccessToken(accessToken);
        tokenSet.setRefreshToken(refreshToken);

        // 유효한 refreshToken을 저장
        refreshTokenRepository.save(user.getId(), refreshToken, REFRESH_TOKEN_VALID_MILLISECOND/1000L);

        // 이전에 발급받은 accessToken들을 무효화하기위해 redis에 현재시간을 저장한다.
        // 토큰 유효성 검사에서 redis에 저장된 시간보다 이전에 발급받은 토큰은 유효하지 않은 토큰으로 취급한다.
        // jwt의 iat는 단위가 초이기 때문에 나누기 1000한 값을 저장
        signOutRepository.save(user.getId(), now.getTime(), ACCESS_TOKEN_VALID_MILLISECOND/1000L);

        return tokenSet;
    }

    // accessToken의 인증 정보를 반환
    public Authentication getAuthentication(String token){
        if(token == null) return null;
        Claims claims = Jwts.parser().setSigningKey(accessSecreteKey).parseClaimsJws(token).getBody();
        List<String> roleList = (List<String>)claims.get(CLAIM_KEY_ROLES);
        Collection<? extends GrantedAuthority> roles =  roleList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(new User(claims),"", roles);
    }

    // refreshToken의 인증 정보를 반환
    public Authentication getAuthenticationFromRefreshToken(String refreshToken){
        if(refreshToken == null) return null;
        Claims claims = Jwts.parser().setSigningKey(refreshSecretKey).parseClaimsJws(refreshToken).getBody();
        List<String> roleList = (List<String>)claims.get(CLAIM_KEY_ROLES);
        Collection<? extends GrantedAuthority> roles =  roleList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(new User(claims),"", roles);
    }

    public String getUserPk(String token){
        return Jwts.parser().setSigningKey(accessSecreteKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String getUserPkFromRefreshToken(String refreshToken){
        return Jwts.parser().setSigningKey(refreshSecretKey).parseClaimsJws(refreshToken).getBody().getSubject();
    }

    public boolean validateToken(String jwtToken){
        try{
            Jws<Claims> claims = Jwts.parser().setSigningKey(accessSecreteKey).parseClaimsJws(jwtToken);
            String userId = claims.getBody().getSubject();
            Long signOutTime = signOutRepository.find(userId);

            // redis에 로그아웃 시간이 있을 경우
            if(signOutTime != null){
                // 토큰 발행일이 마지막 로그아웃 시간보다 이전일 경우 유효하지 않은 것으로 처리한다
                long iat = claims.getBody().getIssuedAt().getTime();
                return iat >= signOutTime;
            }
            return true;
        }catch (Exception e){
            // 유효하지 않은 토큰들은 파싱할때 에러가 발생한다. parseClaimsJws 함수 throws 참고
            return false;
        }
    }

    public boolean validateRefreshToken(String jwtRefreshToken){
        try{
            Jws<Claims> claims = Jwts.parser().setSigningKey(refreshSecretKey).parseClaimsJws(jwtRefreshToken);
            String userId = claims.getBody().getSubject();
            String refreshToken = refreshTokenRepository.find(userId);

            return jwtRefreshToken.equals(refreshToken);
        }catch(Exception e){
            return false;
        }
    }

    public void deleteRefreshToken(String userId){
        refreshTokenRepository.delete(userId);
        Date now = new Date();

        // 발급받은 accessToken들을 무효화하기위해 redis에 현재시간을 저장한다.
        signOutRepository.save(userId, now.getTime(), ACCESS_TOKEN_VALID_MILLISECOND/1000L);
    }
}

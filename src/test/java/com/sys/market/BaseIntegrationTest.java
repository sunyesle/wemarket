package com.sys.market;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sys.market.config.security.JwtTokenProvider;
import com.sys.market.dto.TokenSet;
import com.sys.market.entity.User;
import com.sys.market.enums.UserRole;
import com.sys.market.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.http.Cookie;
import java.util.Collections;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@AutoConfigureMockMvc // MockMvc를 이용한 테스트를 진행하기 위해 필요하다.
//@Disabled // 클래스 또는 테스트 메소드를 실행하지 않는다.
@Transactional // 클래스 내부의 각각의 테스트가 실행될때마다 데이터베이스를 롤백한다.
@ActiveProfiles("test")
public class BaseIntegrationTest {
    @Autowired
    protected MockMvc mvc;
    @Autowired
    protected WebApplicationContext context;
    @Autowired
    protected ObjectMapper objectMapper; // JSON으로 값을 넘길때 사용

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserService userService;

    protected TokenSet tokens;
    protected Cookie accessCookie;
    protected Cookie refreshCookie;

    @BeforeEach
    public void setup(){
        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity()) // security 설정
                .addFilters(new CharacterEncodingFilter("UTF-8", true)) // 한글 깨짐 처리. 인코딩 설정
                .build();

        // 로그인용 유저 세팅
        User userInfo = User.builder()
                .id("user")
                .password("passw0rd")
                .nickname("nickname")
                .addressCode("1111010100")
                .email("email@email.com")
                .role(Collections.singletonList(UserRole.ROLE_USER.name()))
                .build();
        userService.addUser(userInfo);

        tokens = jwtTokenProvider.createTokens(userInfo);
        accessCookie = new Cookie(JwtTokenProvider.ACCESS_TOKEN_NAME, tokens.getAccessToken());
        refreshCookie = new Cookie(JwtTokenProvider.REFRESH_TOKEN_NAME, tokens.getRefreshToken());
    }
}

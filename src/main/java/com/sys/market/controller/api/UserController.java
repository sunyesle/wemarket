package com.sys.market.controller.api;

import com.sys.market.advice.exception.CBadRequestException;
import com.sys.market.advice.exception.CNotFoundException;
import com.sys.market.advice.exception.CSigninFailedException;
import com.sys.market.config.security.JwtTokenProvider;
import com.sys.market.dto.TokenSet;
import com.sys.market.entity.User;
import com.sys.market.dto.result.CommonResult;
import com.sys.market.dto.result.SingleResult;
import com.sys.market.enums.UserStatus;
import com.sys.market.enums.UserRole;
import com.sys.market.repository.RefreshTokenRepository;
import com.sys.market.repository.VerificationCodeRepository;
import com.sys.market.service.UserService;
import com.sys.market.service.ResponseService;
import com.sys.market.util.CookieUtil;
import com.sys.market.util.mail.MailUtil;
import com.sys.market.util.MyUUID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/users")
public class UserController {
    private final UserService userService;
    private final ResponseService responseService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtil cookieUtil;
    private final MailUtil mailUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final VerificationCodeRepository verificationCodeRepository;

    @Operation(summary = "로그인")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signIn")
    public CommonResult signIn(@RequestBody @Valid UserLoginRequest userLoginRequest,
                               HttpServletResponse response){
        User userInfo = userService.signin(userLoginRequest.getId(), userLoginRequest.getPassword());

        TokenSet tokenSet = jwtTokenProvider.createTokens(userInfo);

        // cookie에 토큰을 저장하는 방식
        Cookie accessToken = cookieUtil.createCookie(JwtTokenProvider.ACCESS_TOKEN_NAME,
                tokenSet.getAccessToken(), JwtTokenProvider.ACCESS_TOKEN_VALID_MILLISECOND / 1000);
        Cookie refreshToken = cookieUtil.createCookie(JwtTokenProvider.REFRESH_TOKEN_NAME,
                tokenSet.getRefreshToken(), JwtTokenProvider.REFRESH_TOKEN_VALID_MILLISECOND / 1000);
        refreshToken.setPath("/api/refresh");

        response.addCookie(accessToken);
        response.addCookie(refreshToken);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/signOut")
    public void signOut(@Parameter(hidden = true) @AuthenticationPrincipal User user,
                        HttpServletResponse response){
        if(user == null) throw new CSigninFailedException();

        refreshTokenRepository.delete(user.getId());

        Cookie accessToken = cookieUtil.createCookie(JwtTokenProvider.ACCESS_TOKEN_NAME, null, 0);
        Cookie refreshToken = cookieUtil.createCookie(JwtTokenProvider.REFRESH_TOKEN_NAME, null, 0);

        response.addCookie(accessToken);
        response.addCookie(refreshToken);
        jwtTokenProvider.deleteRefreshToken(user.getId());
    }

    @Operation(summary = "로그아웃")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CommonResult userAdd(@RequestBody @Valid UserRequest userRequest){
        User userInfo = UserRequest.toEntity(userRequest);
        userService.addUser(userInfo);

        sendVerifyMail(userRequest.getId());

        return responseService.getSuccessResult();
    }

    // 권한이 ROLE_UNVERIFIED 일때만 가능
    @Operation(summary = "인증 메일 발송")
    @PostMapping("/verify")
    public void verify(@Parameter(hidden = true) @AuthenticationPrincipal User user){
        sendVerifyMail(user.getId());
    }

    @Operation(summary = "이메일 인증")
    @GetMapping("/verify/{code}")
    public CommonResult verifyEmailAddress(@PathVariable String code){
        String userId = verificationCodeRepository.find(code);
        if(userId == null) throw new CBadRequestException("이메일 인증을 실패했습니다.");

        userService.modifyUserRole(userId, Collections.singletonList(UserRole.ROLE_USER.name()));
        verificationCodeRepository.delete(code);
        
        jwtTokenProvider.deleteRefreshToken(userId); // 권한 정보가 변경되었으므로 로그아웃 처리
        return responseService.getSuccessResult();
    }

    @Operation(summary = "사용자 조회")
    @GetMapping("/{id}")
    public SingleResult<Object> user(@Parameter(hidden = true) @AuthenticationPrincipal User currentUser,
                                     @PathVariable String id){
        User user = userService.findUser(id);
        if(user == null) throw new CNotFoundException("존재하지 않는 사용자입니다.");

        // 자신의 정보를 조회하는 경우
        if(currentUser != null && currentUser.getId().equals(id)){
            UserDetailResponse userDetailResponse = user.toUserDetailResponse();
            return responseService.getSingleResult(userDetailResponse);
        }else{
            UserResponse userResponse = user.toUserResponse();
            return responseService.getSingleResult(userResponse);
        }
    }

    @Operation(summary = "아이디 중복 체크")
    @GetMapping("/{id}/exists")
    public SingleResult<Boolean> idExists(@PathVariable String id){
        return responseService.getSingleResult(userService.idExists(id));
    }

    @Operation(summary = "현재 사용자의 정보 수정")
    @PutMapping
    public CommonResult userModify(@Parameter(hidden = true) @AuthenticationPrincipal User user,
                                   @RequestBody @Valid UserUpdateRequest userUpdateInfo){
        User userInfo = UserUpdateRequest.toEntity(user.getId(), userUpdateInfo);
        userService.modifyUser(userInfo);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "현재 사용자의 이메일 변경")
    @PutMapping("/email")
    public CommonResult userEmailModify(@Parameter(hidden = true) @AuthenticationPrincipal User user,
                                        @RequestBody @Valid UserEmailRequest userEmailRequest){
        denyGuest(user);

        userService.modifyUserEmail(user.getId(),
                userEmailRequest.getEmail());

        sendVerifyMail(user.getId());

        jwtTokenProvider.deleteRefreshToken(user.getId()); // 권한 정보가 변경되었으므로 로그아웃 처리
        return responseService.getSuccessResult();
    }

    @Operation(summary = "현재 사용자의 비밀번호 변경")
    @PutMapping("/password")
    public CommonResult userPasswordModify(@Parameter(hidden = true) @AuthenticationPrincipal User user,
                                           @RequestBody @Valid UserPasswordRequest userPasswordRequest){
        denyGuest(user);

        userService.modifyUserPassword(user.getId(),
                userPasswordRequest.getOldPassword(),
                userPasswordRequest.getPassword());

        return responseService.getSuccessResult();
    }

    @Operation(summary = "현재 사용자의 프로필 사진 변경")
    @PutMapping(value = "/profile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CommonResult userProfileImageModify(@Parameter(hidden = true) @AuthenticationPrincipal User user,
                                               @RequestParam(value = "file", required = false) MultipartFile file){
        userService.modifyUserProfileImage(user.getId(), file);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "현재 사용자 삭제")
    @DeleteMapping
    public CommonResult userRemove(@Parameter(hidden = true) @AuthenticationPrincipal User user,
                                   @RequestBody @Valid UserDeleteRequest userLoginRequest,
                                   HttpServletResponse response){
        denyGuest(user);

        User userInfo = userService.signin(user.getId(), userLoginRequest.getPassword());

        userService.removeUser(userInfo);
        jwtTokenProvider.deleteRefreshToken(user.getId());

        // 로그아웃 처리
        refreshTokenRepository.delete(user.getId());
        Cookie accessToken = cookieUtil.createCookie(JwtTokenProvider.ACCESS_TOKEN_NAME, null, 0);
        Cookie refreshToken = cookieUtil.createCookie(JwtTokenProvider.REFRESH_TOKEN_NAME, null, 0);
        response.addCookie(accessToken);
        response.addCookie(refreshToken);

        return responseService.getSuccessResult();
    }

    private void sendVerifyMail(String userId){
        String email = userService.findUser(userId).getEmail();

        String code = MyUUID.getRandomString();
        verificationCodeRepository.save(code, userId, 60L*60*24);

        String verifyUrl = "https://sunys.xyz/verify/" + code;

        StringBuilder body = new StringBuilder();
        body.append("<div style=\"font-family: 'Apple SD Gothic Neo', 'sans-serif' !important; width: 540px; height: 600px; border-top: 4px solid #5e72e4; margin: 100px auto; padding: 30px 0; box-sizing: border-box;\">\n");
        body.append("<h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400;\">\n");
        body.append("<span style=\"font-size: 15px; margin: 0 0 10px 3px;\">WeMarket</span><br />\n");
        body.append("<span style=\"color: #5e72e4;\">이메일인증</span> 안내입니다.\n");
        body.append("</h1>\n");
        body.append("<p style=\"font-size: 16px; line-height: 26px; margin-top: 50px; padding: 0 5px;\">\n");
        body.append("안녕하세요.<br />\n");
        body.append("위마켓을 이용해 주셔서 진심으로 감사드립니다.<br />\n");
        body.append("아래 <b style=\"color: #5e72e4;\">'이메일 인증'</b> 버튼을 클릭하여 회원가입을 완료해 주세요.<br />\n");
        body.append("감사합니다.\n");
        body.append("</p>\n");
        body.append("<a style=\"color: #FFF; text-decoration: none; text-align: center;\" href=\"");
        body.append(verifyUrl);
        body.append("\" target=\"_blank\"><p style=\"display: inline-block; width: 210px; height: 45px; margin: 30px 5px 40px; background: #5e72e4; line-height: 45px; vertical-align: middle; font-size: 16px;\">이메일 인증</p></a>\n");
        body.append("<div style=\"border-top: 1px solid #DDD; padding: 5px;\">\n");
        body.append("<p style=\"font-size: 13px; line-height: 21px; color: #555;\">\n");
        body.append("만약 버튼이 정상적으로 클릭되지 않는다면, 아래 링크를 복사하여 접속해 주세요.<br />\n");
        body.append(verifyUrl);
        body.append("</p>\n");
        body.append("</div>\n");
        body.append("</div>\n");

        mailUtil.sendMail(email, "[위마켓] 이메일 인증", body.toString());
    }

    private void denyGuest(User user){
        if(user.getRole().contains(UserRole.ROLE_GUEST.toString()))
            throw new CBadRequestException("게스트 계정은 사용할 수 없는 기능입니다.");
    }

    @Getter
    @Setter
    public static class UserRequest {
        @NotBlank(message = "아이디를 입력해주세요.")
        private String id;

        @NotNull(message = "비밀번호를 입력해주세요.")
        @Pattern(regexp = "(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z0-9]{8,16}", message = "비밀번호는 영문, 숫자를 포함해서 8~16자 이내로 입력해주세요.")
        private String password;

        @NotBlank(message = "닉네임을 입력해주세요.")
        @Size(min=2, max=10, message = "닉네임은 2~10자 이내로 입력해주세요.")
        private String nickname;

        @NotBlank(message = "이메일을 입력해주세요.") // @Email은 null을 유효하다고 판단하기때문에 추가함.
        @Email(message = "이메일 형식으로 입력해주세요.")
        private String email;

        @NotBlank(message = "주소코드를 입력해주세요.")
        @Pattern(regexp = "[0-9]{10}", message = "주소코드 형식에 맞지 않습니다.")
        private String addressCode;

        public static User toEntity(UserRequest u){
            return User.builder()
                    .id(u.getId())
                    .password(u.getPassword())
                    .nickname(u.getNickname())
                    .addressCode(u.getAddressCode())
                    .email(u.getEmail())
                    .role(Collections.singletonList(UserRole.ROLE_UNVERIFIED.name()))
                    .build();
        }
    }

    @Getter
    @Setter
    public static class UserLoginRequest {
        @NotNull(message = "아이디를 입력해주세요.")
        private String id;

        @NotNull(message = "비밀번호를 입력해주세요.")
        private String password;
    }

    @Getter
    @Setter
    public static class UserPasswordRequest {
        @NotNull(message = "비밀번호를 입력해주세요.")
        private String oldPassword;

        @NotNull(message = "새로운 비밀번호를 입력해주세요.")
        @Pattern(regexp = "(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z0-9]{8,16}", message = "비밀번호는 영문, 숫자를 포함해서 8~16자 이내로 입력해주세요.")
        private String password;
    }

    @Getter
    @Setter
    public static class UserEmailRequest {
        @NotBlank(message = "이메일을 입력해주세요.") // @Email은 null을 유효하다고 판단하기때문에 추가함.
        @Email(message = "이메일 형식으로 입력해주세요.")
        private String email;
    }

    @Getter
    @Setter
    public static class UserUpdateRequest {
        @Size(min=2, max=10, message = "닉네임은 2~10자 이내로 입력해주세요.")
        private String nickname;

        @Pattern(regexp = "[0-9]{10}", message = "주소코드 형식에 맞지 않습니다.")
        private String addressCode;

        @Size(max=40, message = "프로필설명은 400자 이내로 입력해주세요.")
        private String bio;

        public static User toEntity(String id, UserUpdateRequest request){
            return User.builder()
                    .id(id)
                    .nickname(request.getNickname())
                    .addressCode(request.getAddressCode())
                    .bio(request.getBio()).build();
        }
    }

    @Getter
    @Setter
    public static class UserDeleteRequest {
        @NotNull(message = "비밀번호를 입력해주세요.")
        private String password;
    }

    @Getter
    @Setter
    public static class UserResponse {
        private String id;
        private String nickname;
        private String bio;
        private String profileImage;
        private UserStatus status;
        private List<String> role;
        private Date createDate;
        private Date updateDate;
        private String address; // association 으로 받아온 값을 담을 필드. 주소 이름
        private Double rating;
    }

    @Getter
    @Setter
    public static class UserDetailResponse {
        private String id;
        private String nickname;
        private String email;
        private String bio;
        private String profileImage;
        private UserStatus status;
        private List<String> role;
        private Date createDate;
        private Date updateDate;
        private Date passwordChangeDate;
        private String address; // association 으로 받아온 값을 담을 필드. 주소 이름
        private Double rating;
    }
}

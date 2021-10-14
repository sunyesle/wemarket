package com.sys.market.advice;

import com.sys.market.advice.exception.*;
import com.sys.market.dto.result.CommonResult;
import com.sys.market.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionAdvice {

    private final ResponseService responseService;


    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(CSigninFailedException.class)
    public CommonResult signinFailedException(){
        return responseService.getFailResult("아이디 또는 비밀번호가 일치하지 않습니다.");
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CUserExistsException.class)
    public CommonResult userExistsException(){
        return responseService.getFailResult("이미 가입한 회원입니다.");
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(CJwtException.class)
    public CommonResult jwtException(){
        return responseService.getFailResult("jwt 오류");
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(CAccessDeniedException.class)
    public CommonResult accessDeniedException(){
        return responseService.getFailResult("해당 리소스에 대한 접근 권한이 없습니다.");
    }

    // @PreAuthorize에서 에러가 났을 때
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public CommonResult accessDeniedAnnotation(){
        return responseService.getFailResult("해당 리소스에 대한 접근 권한이 없습니다.");
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(CAuthenticationEntryPointException.class)
    public CommonResult authenticationEntryPointException(){
        return responseService.getFailResult("jwt 토큰이 없거나, 유효하지 않습니다.");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CNotFoundException.class)
    public CommonResult notFoundException(){
        return responseService.getFailResult("존재하지 않습니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CBadRequestException.class)
    public CommonResult badRequestException(CBadRequestException e) {
        String msg = (e.getMessage() == null)? "유효하지 않은 요청입니다." : e.getMessage();
        return responseService.getFailResult(msg);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CConflictException.class)
    public CommonResult conflictException(CConflictException e) {
        String msg = (e.getMessage() == null)? "중복된 id 입니다." : e.getMessage();
        return responseService.getFailResult(msg);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CInvalidImageFormatException.class)
    public CommonResult badImageFormatException(){
        return responseService.getFailResult("이미지 파일이 손상되었거나 잘못된 형식입니다.");
    }

    // @Valid 주석이 달린 인수에 대한 유효성 검사가 실패했을때 발생한다.
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult methodArgumentNotValidException(MethodArgumentNotValidException e){
         String errorMsg = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
         return responseService.getFailResult(errorMsg);
    }

    // @Validated 주석이 달린 인수에 대한 유효성 검사가 실패했을때 발생한다.
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public CommonResult constraintViolationException(ConstraintViolationException e){
        String errorMsg = e.getConstraintViolations().iterator().next().getMessage();
        return responseService.getFailResult(errorMsg);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CMessagingException.class)
    public CommonResult messagingException(){
        return responseService.getFailResult("메일 전송 실패");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public CommonResult httpMessageNotReadableException(HttpMessageNotReadableException e){
        e.printStackTrace();
        return responseService.getFailResult("JSON parse error");
    }
}

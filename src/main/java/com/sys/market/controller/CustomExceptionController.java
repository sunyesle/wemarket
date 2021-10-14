package com.sys.market.controller;

import com.sys.market.advice.exception.CAccessDeniedException;
import com.sys.market.advice.exception.CAuthenticationEntryPointException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/exception")
public class CustomExceptionController {

    @RequestMapping(value = "/entrypoint")
    public void authenticationEntrypointException(HttpServletResponse response) {
        throw new CAuthenticationEntryPointException();
    }

    @RequestMapping(value="/accessdenied")
    public void accessDeniedException(){
        throw new CAccessDeniedException();
    }
}
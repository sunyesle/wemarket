package com.sys.market.config;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// freemarker에서 security taglibrary 사용을 위한 세팅
public class ClassPathTldsLoader {
    private static final String SECURITY_TLD = "/META-INF/security.tld";

    private final List<String> classPathTlds;

    public ClassPathTldsLoader(String... classPathTlds) {
        super();
        if(ArrayUtils.isEmpty(classPathTlds)){
            this.classPathTlds = Collections.singletonList(SECURITY_TLD);
        }else{
            this.classPathTlds = Arrays.asList(classPathTlds);
        }
    }

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @PostConstruct
    public void loadClassPathTlds(){
        freeMarkerConfigurer.getTaglibFactory().setClasspathTlds(classPathTlds);
    }
}

package com.sys.market.util;

import java.util.UUID;

public class MyUUID {
    private MyUUID(){
        throw new IllegalStateException("Utility class");
    }

    public static String getRandomString(){
        return UUID.randomUUID().toString().replace("-","");
    }
}

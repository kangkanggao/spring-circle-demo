package com.highlion.aop;

import com.highlion.aop.annotation.After;
import com.highlion.aop.annotation.Aspect;
import com.highlion.aop.annotation.Before;

/**
 * @Description
 * @Date 2020/8/26 17:34
 * @Created by gkk
 */
@Aspect
public class AspectDemo {
    @Before("test")
    public void before() {
        System.out.println("before...");
    }

    @After("test")
    public void after() {
        System.out.println("after...");
    }
}

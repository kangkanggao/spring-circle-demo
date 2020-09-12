package com.highlion.service;


import com.highlion.beans.annotation.Component;
import com.highlion.beans.annotation.Resource;

/**
 * @Description
 * @Date 2020/8/20 11:30
 * @Created by gkk
 */
@Component
public class CService {
    @Resource
    private AService aService;

    public AService getaService() {
        return aService;
    }

    public String getCString() {
        System.out.println("cccccccccc");
        return "ok";
    }

    public void test() {
        System.out.println("testC");
    }
}

package com.highlion.service;


import com.highlion.beans.annotation.Component;
import com.highlion.beans.annotation.Resource;

/**
 * @Description
 * @Date 2020/8/20 11:30
 * @Created by gkk
 */
@Component
public class BService {
    @Resource
    private CService cService;

    public void test() {
        System.out.println("testB");
    }
}

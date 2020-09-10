package com.highlion.service;

import com.highlion.beans.annotation.Component;
import com.highlion.beans.annotation.Resource;

/**
 * @Description
 * @Date 2020/8/20 11:30
 * @Created by gkk
 */
@Component
public class AService {
    @Resource
    private BService bService;

    public String getString(String objectFactory) {
        System.out.println("aaaaaaaaa" + objectFactory);
        return objectFactory;
    }
}

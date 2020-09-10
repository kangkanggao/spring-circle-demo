package com.highlion.test;

import com.highlion.service.AService;

/**
 * @Description
 * @Date 2020/9/6 21:12
 * @Created by gkk
 */
public class Test2 {
    public static void main(String[] args) {
        Class<?> aClass = PacketScanUtils.scanPacket(AService.class);
        String name = aClass.getName();
        String simpleName = aClass.getSimpleName();
        System.out.println(name);
        System.out.println(simpleName);
    }
}

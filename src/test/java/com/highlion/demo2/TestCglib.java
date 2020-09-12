package com.highlion.demo2;

/**
 * @Description
 * @Date 2020/9/10 13:57
 * @Created by gkk
 */
public class TestCglib {
    public static void main(String[] args) {
        UserServiceCglib cglib = new UserServiceCglib();
        UserServiceImpl bookFacedImpl = (UserServiceImpl) cglib.getInstance(new UserServiceImpl());
        bookFacedImpl.addUser();
    }
}

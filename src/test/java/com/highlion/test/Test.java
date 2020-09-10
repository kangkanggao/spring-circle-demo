package com.highlion.test;

import java.util.function.Function;

/**
 * @Description
 * @Date 2020/8/20 14:59
 * @Created by gkk
 */
public class Test {
    public static void main(String[] args) throws Exception {
        System.setSecurityManager(new SecurityManager());
        SecurityManager securityManager = System.getSecurityManager();
        System.out.println(securityManager);


    }

    enum AB {
        AA,
        BB
    }

    public void getFunction() {
        Function<String, String> function = (a) -> a + "aa";
        String aaa = function.apply("aaa");
        System.out.println(aaa);
    }


    public String getString2(Function<String, String> function2) {
        return function2.apply("ccc");
    }

    public static class KeyValue {
        private String key;
        private String value;

        public KeyValue(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + ":" + this.value;
        }
    }
}

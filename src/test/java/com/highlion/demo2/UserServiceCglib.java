package com.highlion.demo2;


import com.highlion.exception.GlobalException;
import com.sun.istack.internal.Nullable;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Description
 * @Date 2020/9/10 13:56
 * @Created by gkk
 */
public class UserServiceCglib implements MethodInterceptor {

    private Object targets;

    public Object getInstance(Object target) {
        this.targets = target;
        try {
            Field[] fields = target.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals("user")) {
                    field.setAccessible(true);
                    field.set(targets, new User(1L, "aa", 2));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.targets.getClass());
        // 设置回调方法
        enhancer.setCallback(this);
        // 创建代理对象
        return enhancer.create();
    }

    /**
     * 实现MethodInterceptor接口中重写的方法
     * <p>
     * 回调方法
     */
    @Override
    public Object intercept(Object object, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("事务开始。。。");
        //Object result = proxy.invokeSuper(object, args);
        Object retVal = proxy.invoke(this.targets, args);
        System.out.println("事务结束。。。");
        return processReturnType(proxy, this.targets, method, retVal);
    }

    private static Object processReturnType(
            Object proxy, @Nullable Object target, Method method, @Nullable Object returnValue) {

        // Massage return value if necessary
        if (returnValue != null && returnValue == target) {
            // Special case: it returned "this". Note that we can't help
            // if the target sets a reference to itself in another returned object.
            returnValue = proxy;
        }
        Class<?> returnType = method.getReturnType();
        if (returnValue == null && returnType != Void.TYPE && returnType.isPrimitive()) {
            throw new GlobalException(
                    "Null return value from advice does not match primitive return type for: " + method);
        }
        return returnValue;
    }

}

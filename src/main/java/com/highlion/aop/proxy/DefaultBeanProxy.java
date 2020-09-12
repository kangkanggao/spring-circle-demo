package com.highlion.aop.proxy;
import com.highlion.aop.util.AspectUtils;
import com.highlion.exception.GlobalException;
import com.sun.istack.internal.Nullable;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;


public class DefaultBeanProxy implements MethodInterceptor {
    private Object target;

    public void setTarget(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Method beforeMethod = AspectUtils.getBeforeAdvisorMethod(method.getName());
        if (beforeMethod != null) {
            beforeMethod.invoke(AspectUtils.getAdvisorInstance(beforeMethod.getDeclaringClass()), args);
        }

        Object retVal = methodProxy.invoke(this.target, args);
        Method afterMethod = AspectUtils.getAfterAdvisorMethod(method.getName());
        if (afterMethod != null) {
            afterMethod.invoke(AspectUtils.getAdvisorInstance(afterMethod.getDeclaringClass()), args);
        }
        return processReturnType(methodProxy, this.target, method, retVal);
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

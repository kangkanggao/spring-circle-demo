package com.highlion.aop.proxy;
import com.highlion.aop.util.AspectUtils;
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

        Object result = methodProxy.invokeSuper(object, args);

        Method afterMethod = AspectUtils.getAfterAdvisorMethod(method.getName());
        if (afterMethod != null) {
            afterMethod.invoke(AspectUtils.getAdvisorInstance(afterMethod.getDeclaringClass()), args);
        }

        return result;
    }
}

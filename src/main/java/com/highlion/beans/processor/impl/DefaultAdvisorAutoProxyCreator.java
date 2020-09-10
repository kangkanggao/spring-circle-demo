package com.highlion.beans.processor.impl;

import com.sun.istack.internal.Nullable;
import com.highlion.aop.proxy.ProxyInstance;
import com.highlion.aop.util.AspectUtils;
import com.highlion.beans.processor.BeanPostProcessor;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class DefaultAdvisorAutoProxyCreator implements BeanPostProcessor {
    private final Set<Object> earlyProxyReferences = Collections.newSetFromMap(new ConcurrentHashMap<>(16));
    private final Map<Object, Boolean> advisedBeans = new ConcurrentHashMap<>(256);
    @Nullable
    protected static final Object[] DO_NOT_PROXY = null;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean != null) {
            Object cacheKey = getCacheKey(bean.getClass(), beanName);
            if (!this.earlyProxyReferences.contains(cacheKey)) {
                return wrapIfNecessary(bean, beanName, cacheKey);
            }
        }
        return bean;
    }

    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) {
        Object cacheKey = getCacheKey(bean.getClass(), beanName);
        if (!this.earlyProxyReferences.contains(cacheKey)) {
            this.earlyProxyReferences.add(cacheKey);
        }
        return wrapIfNecessary(bean, beanName, cacheKey);
    }

    protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {

        if (Boolean.FALSE.equals(this.advisedBeans.get(cacheKey))) {
            return bean;
        }

        // Create proxy if we have advice.
        //判断是否存在有通知的 代理对象  在这里就简化一下

        Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(bean.getClass(), beanName);
        if (specificInterceptors != DO_NOT_PROXY) {
            this.advisedBeans.put(cacheKey, Boolean.TRUE);
            Object proxyBean = new ProxyInstance().getProxy(bean);
            return proxyBean == null ? bean : proxyBean;
        }

        this.advisedBeans.put(cacheKey, Boolean.FALSE);
        return bean;
    }

    Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName) {
        Set<String> allMethodNames = new HashSet<>();
        Set<String> methodNames = AspectUtils.getAdvisorBeforeMap().keySet();
        Set<String> methodNames2 = AspectUtils.getAdvisorAfterMap().keySet();
        allMethodNames.addAll(methodNames);
        allMethodNames.addAll(methodNames2);

        Method[] declaredMethods = beanClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            declaredMethod.setAccessible(true);
            String name = declaredMethod.getName();
            for (String method : allMethodNames) {
                if (name.equals(method)) {
                    return new Object[1];
                }
            }

        }
        return null;
    }

    protected Object getCacheKey(Class<?> beanClass, @Nullable String beanName) {

        return beanClass;

    }
}

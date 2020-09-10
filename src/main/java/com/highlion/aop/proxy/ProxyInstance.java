package com.highlion.aop.proxy;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;


public class ProxyInstance {

    private DefaultBeanProxy beanProxy = new DefaultBeanProxy();

    public Object getProxy(Object bean) {
        beanProxy.setTarget(bean);
        Enhancer en = new Enhancer();
        en.setSuperclass(bean.getClass());
        en.setCallbacks(new Callback[]{beanProxy});
        return en.create();
    }
}

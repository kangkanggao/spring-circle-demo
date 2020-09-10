package com.highlion.beans.processor;


public interface BeanPostProcessor {
    Object postProcessAfterInitialization(Object bean, String beanName);

    Object getEarlyBeanReference(Object exposedObject, String beanName);
}

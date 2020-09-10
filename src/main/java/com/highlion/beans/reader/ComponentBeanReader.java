package com.highlion.beans.reader;

import com.highlion.beans.annotation.Component;
import com.highlion.beans.annotation.Controller;
import com.highlion.beans.model.BeanDefinition;
import com.highlion.beans.support.impl.DefaultBeanFactory;
import com.highlion.util.ReflectionUtils;
import com.highlion.util.StringUtils;

import java.util.Set;


public class ComponentBeanReader {

    public void readBeanDefinition(DefaultBeanFactory beanFactory) {
        Set<Class<?>> componentSet = ReflectionUtils.getAllClass(Component.class);
        Set<Class<?>> controllerSet = ReflectionUtils.getAllClass(Controller.class);
        componentSet.addAll(controllerSet);
        componentSet.forEach((componentClass) -> {
            BeanDefinition beanDefinition = new BeanDefinition();
            String beanName = componentClass.getAnnotation(Component.class) != null ? componentClass.getAnnotation(Component.class).value() : componentClass.getAnnotation(Controller.class).value();
            if ("".equals(beanName)) {
                beanName = StringUtils.lowerFirst(componentClass.getSimpleName());
            }
            beanDefinition.setBeanClass(componentClass);
            beanFactory.registerBeanDefinition(beanName, beanDefinition);
        });
    }
}

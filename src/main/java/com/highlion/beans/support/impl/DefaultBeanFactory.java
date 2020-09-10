package com.highlion.beans.support.impl;


import com.highlion.beans.annotation.Resource;
import com.highlion.beans.model.BeanDefinition;
import com.highlion.beans.processor.BeanPostProcessor;
import com.highlion.beans.processor.impl.DefaultAdvisorAutoProxyCreator;
import com.highlion.beans.support.BeanFactory;
import com.highlion.beans.support.ObjectFactory;
import com.highlion.beans.support.SingletonBeanRegistry;
import com.highlion.beans.util.BeanUtils;
import com.highlion.exception.GlobalException;
import com.highlion.util.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanFactory extends SingletonBeanRegistry implements BeanFactory {

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(beanName, beanDefinition);
    }

    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.add(beanPostProcessor);
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return beanPostProcessors;
    }

    public void preInstantiateSingletons() {
        this.beanDefinitionMap.forEach((beanName, beanDef) -> {
            getBean(beanName);
        });
    }

    @Override
    public Object getBean(String name) {
        return doGetBean(name);
    }

    @SuppressWarnings("unchecked")
    private <T> T doGetBean(String beanName) {
        Object bean;
        Object sharedInstance = getSingleton(beanName, true);
        if (sharedInstance != null) {
            bean = sharedInstance;
        } else {
            BeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
            if (beanDefinition == null) {
                throw new GlobalException("can not find the definition of bean '" + beanName + "'");
            }
            // 就不判断是否是单例的了  就统一单例的
            bean = getSingleton(beanName, () -> {
                try {
                    // Spring在这里调用 createBean方法 其实就是调用 doCreateBean方法
                    return doCreateBean(beanName, beanDefinition);
                } catch (Exception ex) {
                    removeSingleton(beanName);
                    throw ex;
                }
            });
        }
        return (T) bean;
    }

    private Object doCreateBean(String beanName, BeanDefinition beanDefinition) {
        // Spring 创建的 是包装bean 在这里就简化一下 直接创建bean 如果是将被代理的bean 实际上在这个阶段 仅仅创建的是原生的bean
        Object bean = createBeanInstance(beanName, beanDefinition);
        //Spring 判断是否是单例 是否支持循环依赖 是否是正在创建的bean  在这里就简化一下 直接就是 判断是否正在创建的bean
        boolean earlySingletonExposure = isSingletonCurrentlyInCreation(beanName);
        if (earlySingletonExposure) {
            // getEarlyBeanReference 方法的参数的话 在这里比Spring少个bd 简化一下
            ObjectFactory objectFactory = () -> getEarlyBeanReference(beanName, bean);
            addSingletonFactory(beanName, objectFactory);
        }
        Object exposedObject = bean;
        // 给原生bean赋予属性值
        populateBean(beanName, beanDefinition, bean);
        exposedObject = initializeBean(exposedObject, beanName);
        if (earlySingletonExposure) {
            Object earlySingletonReference = getSingleton(beanName, false);
            if (earlySingletonReference != null) {
                if (exposedObject == bean) {
                    exposedObject = earlySingletonReference;
                }
            }
        }
        return exposedObject;
    }

    protected Object initializeBean(final Object bean, final String beanName) {

        Object wrappedBean = bean;


        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);

        return wrappedBean;
    }

    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) {

        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessAfterInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }

    public Object getEarlyBeanReference(String beanName, Object bean) {
        Object exposedObject = bean;
        // 如果是代理 Spring 会去执行 AnnotationAwareAspectJAutoProxyCreator后置处理器去处理
        // 这里直接就简单用 DefaultAdvisorAutoProxyCreator 处理器去处理
        for (BeanPostProcessor bp : getBeanPostProcessors()) {
            if (bp instanceof DefaultAdvisorAutoProxyCreator) {
                exposedObject = bp.getEarlyBeanReference(bean, beanName);
            }
        }
        return exposedObject;
    }

    private Object createBeanInstance(String beanName, BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Constructor<?> constructorToUse;
        if (beanClass.isInterface()) {
            throw new GlobalException("Specified class '" + beanName + "' is an interface");
        }
        try {
            constructorToUse = beanClass.getDeclaredConstructor((Class<?>[]) null);
            return BeanUtils.instantiateClass(constructorToUse);
        } catch (Exception e) {
            throw new GlobalException("'" + beanName + "',No default constructor found", e);
        }
    }

    // Spring会走一套比较复杂的流程交由后置处理器(CommonAnnotationBeanPostProcessor) 去给属性赋值  比如一般的属性赋值 还有集合类型的属性赋值等等操作
    // 这里的话 直接简单粗暴 简单化
    private void populateBean(String beanName, BeanDefinition beanDefinition, Object beanInstance) {
        Field[] beanFields = beanDefinition.getBeanClass().getDeclaredFields();
        try {
            for (Field field : beanFields) {
                if (field.getAnnotation(Resource.class) == null) {
                    continue;
                }
                if (!containsBean(field.getName())) {
                    throw new GlobalException("'@Resource' for field '" + field.getClass().getName() + "' can not find");
                }
                field.setAccessible(true);
                field.set(beanInstance, getBean(field.getName()));
            }
        } catch (Exception e) {
            throw new GlobalException("populateBean '" + beanName + "' error", e);
        }
    }


    private boolean containsBeanDefinition(String name) {
        return beanDefinitionMap.containsKey(name);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) getBean(StringUtils.lowerFirst(requiredType.getSimpleName()));
    }

    @Override
    public boolean containsBean(String name) {
        return this.containsSingleton(name) || this.containsBeanDefinition(name);
    }
}

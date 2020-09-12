package com.highlion.beans;

import com.highlion.beans.processor.impl.DefaultAdvisorAutoProxyCreator;
import com.highlion.beans.reader.ComponentBeanReader;
import com.highlion.beans.support.BeanFactory;
import com.highlion.beans.support.impl.DefaultBeanFactory;


public class ApplicationContext implements BeanFactory {

    private DefaultBeanFactory beanFactory = new DefaultBeanFactory();

    public ApplicationContext() {
        loadBeanDefinitions(beanFactory);
        postProcessBeanFactory(beanFactory);
        finishBeanFactoryInitialization(beanFactory);
    }

    private void loadBeanDefinitions(DefaultBeanFactory beanFactory) {
        ComponentBeanReader beanReader = new ComponentBeanReader();
        beanReader.readBeanDefinition(beanFactory);
    }

    private void postProcessBeanFactory(DefaultBeanFactory beanFactory) {
        beanFactory.addBeanPostProcessor(new DefaultAdvisorAutoProxyCreator());
    }

    private void finishBeanFactoryInitialization(DefaultBeanFactory beanFactory) {
        beanFactory.preInstantiateSingletons();
    }

    @Override
    public Object getBean(String name) {
        return getBeanFactory().getBean(name);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        return getBeanFactory().getBean(requiredType);
    }

    @Override
    public boolean containsBean(String name) {
        return getBeanFactory().containsBean(name);
    }

    private DefaultBeanFactory getBeanFactory() {
        return beanFactory;
    }
}

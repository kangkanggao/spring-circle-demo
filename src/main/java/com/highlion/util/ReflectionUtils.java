package com.highlion.util;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;


public class ReflectionUtils {

    private static final Reflections reflections = new Reflections("");

    public static Set<Class<?>> getAllClass(Class<? extends Annotation> clazz) {
        return reflections.getTypesAnnotatedWith(clazz);
    }
}

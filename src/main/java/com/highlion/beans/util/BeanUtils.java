package com.highlion.beans.util;

import com.highlion.exception.GlobalException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


public class BeanUtils {

    public static <T> T instantiateClass(Constructor<T> ctor, Object... args){
        try {
            ctor.setAccessible(true);
            return ctor.newInstance(args);
        }
        catch (InstantiationException ex) {
            throw new GlobalException("'"+ctor.getName()+"',Is it an abstract class?", ex);
        }
        catch (IllegalAccessException ex) {
            throw new GlobalException("'"+ctor.getName()+",Is the constructor accessible?", ex);
        }
        catch (IllegalArgumentException ex) {
            throw new GlobalException("'"+ctor.getName()+",Illegal arguments for constructor", ex);
        }
        catch (InvocationTargetException ex) {
            throw new GlobalException("'"+ctor.getName()+",Constructor threw exception", ex.getTargetException());
        }
    }
}

package com.highlion.beans.util;

import com.highlion.beans.ApplicationContext;


public class ApplicationContextUtils {

    private static ApplicationContext applicationContext;

    public static void refresh() {
        applicationContext = new ApplicationContext();
    }

    public static ApplicationContext getContext() {
        return applicationContext;
    }
}

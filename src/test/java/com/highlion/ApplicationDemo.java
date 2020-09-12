package com.highlion;


import com.highlion.beans.ApplicationContext;
import com.highlion.service.BService;
import com.highlion.service.CService;
import com.highlion.service.AService;

/**
 * @Description
 * @Date 2020/8/20 11:30
 * @Created by gkk
 */
public class ApplicationDemo {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ApplicationContext();
        AService aService = applicationContext.getBean(AService.class);
        BService bService = applicationContext.getBean(BService.class);
        CService cService = applicationContext.getBean(CService.class);

        System.out.println(aService.getbService());
        System.out.println(bService.getcService());
        System.out.println(cService.getaService());
        cService.test();
        bService.test();
        aService.getString();
    }
}

package com.servlet.chapter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by Mr.PanYang on 2018/5/11.
 */
public class DynamicContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(final ServletContextEvent servletContextEvent) {
        System.out.println("DynamicContextListener init");
    }

    @Override
    public void contextDestroyed(final ServletContextEvent servletContextEvent) {
        System.out.println("DynamicContextListener destroy");
    }
}

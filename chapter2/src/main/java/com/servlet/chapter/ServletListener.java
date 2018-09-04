package com.servlet.chapter;

import javax.servlet.*;
import javax.servlet.annotation.WebListener;

/**
 * Created by Mr.PanYang on 2018/5/11.
 */
@WebListener
public class ServletListener implements ServletContextListener {
    @Override
    public void contextInitialized(final ServletContextEvent servletContextEvent) {
        ServletContext sc = servletContextEvent.getServletContext();

        //Once the first ServletContextListener has been called, no more ServletContextListeners may be added.
        sc.addListener("com.dispacher.chapter.DynamicContextListener");

        sc.addFilter("DynamicFilter", DynamicFilter.class);

        ServletRegistration.Dynamic dynamic1 = sc.addServlet("DynamicServlet1", DynamicServlet.class);
        dynamic1.setLoadOnStartup(1);
        dynamic1.addMapping("/dynamic1");

        sc.addServlet("DynamicServlet2", "com.dispacher.chapter.DynamicServlet").addMapping("/dynamic2");

        sc.addServlet("DynamicServlet3", new DynamicServlet()).addMapping("/dynamic3");
    }

    @Override
    public void contextDestroyed(final ServletContextEvent servletContextEvent) {
        System.out.println("ServletListener destroy");
    }
}

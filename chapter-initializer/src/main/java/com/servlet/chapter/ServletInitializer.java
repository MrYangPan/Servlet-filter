package com.servlet.chapter;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.Set;

/**
 * Created by Mr.PanYang on 2018/5/14.
 */
public class ServletInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        System.out.println("MyServletContainerInitializer init");
        ServletRegistration.Dynamic dynamic = ctx.addServlet("dynamicServlet4", DynamicServlet.class);
        dynamic.addMapping("/dynamic4");

        ctx.getServletRegistrations().get("dynamicServlet4").addMapping("/dynamic41");
    }

}

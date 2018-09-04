package com.servlet.chapter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * Created by Mr.PanYang on 2018/5/14.
 */
@WebFilter(filterName = "webFilter", urlPatterns = "/*")
public class WebHttpFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("init  Filter");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("do  Filter");
    }

    @Override
    public void destroy() {
        System.out.println("destroy  Filter");
    }
}

package com.servlet.chapter.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * Created by Mr.PanYang on 2018/5/15.
 */
@WebFilter(
        filterName = "asyncFilter1",
        urlPatterns = {"/filter1", "/filter2"},
        asyncSupported = true,
        dispatcherTypes = {})
public class AsyncFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("====before filter1");
        chain.doFilter(request, response);
        System.out.println("====after filter1");
    }

    @Override
    public void destroy() {

    }
}

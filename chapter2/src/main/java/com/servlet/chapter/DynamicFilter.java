package com.servlet.chapter;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by Mr.PanYang on 2018/5/11.
 */
public class DynamicFilter implements Filter {
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(final ServletRequest servletRequest,final ServletResponse servletResponse,final FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Dynamic Filter");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}

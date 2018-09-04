package com.servlet.chapter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Mr.PanYang on 2018/5/11.
 */
@WebFilter(filterName = "webFilter", urlPatterns = "/*"
//        , dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD}
)
public class ServletFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        System.out.println("filter1===" + httpServletRequest.getRequestURI());
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        System.out.println("ServletFilter 执行 destroy");
    }
}

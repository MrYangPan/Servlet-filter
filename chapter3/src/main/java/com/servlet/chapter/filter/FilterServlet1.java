package com.servlet.chapter.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Mr.PanYang on 2018/5/15.
 */
@WebServlet(name = "filterServlet1", urlPatterns = "/filter1", asyncSupported = true)
public class FilterServlet1 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("filter servlet 1  before");
        final AsyncContext asyncContext = req.startAsync();
        asyncContext.setTimeout(2 * 1000);
        asyncContext.addListener(new AsyncListener() {
            @Override
            public void onComplete(AsyncEvent event) throws IOException {
                System.out.println("do  onComplete");
            }

            @Override
            public void onTimeout(AsyncEvent event) throws IOException {

            }

            @Override
            public void onError(AsyncEvent event) throws IOException {

            }

            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {

            }
        });

        System.out.println("filter servlet 1  before dispatch");
        //异步下分派过去的请求，异步filter也可以拦截
        //如果filter不是DispatcherType.ASYNC 类型，那么分派到/filter2时，拦截器不会调用
        asyncContext.dispatch("/filter2");
        System.out.println("filter servlet 1  after dispatch");

        System.out.println("filter servlet 1  after");
    }
}
package com.servlet.chapter.filter;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Mr.PanYang on 2018/5/15.
 */
@WebServlet(name = "filterServlet2", urlPatterns = "/filter2", asyncSupported = true)
public class FilterServlet2 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("filter servlet 2  before");
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

        System.out.println("filter servlet 2  before dispatch");
        req.setAttribute("msg", "success");
        asyncContext.dispatch("/WEB-INF/jsp/dispatch.jsp");
        System.out.println("filter servlet 2  after dispatch");

        System.out.println("filter servlet 2  after");
    }
}

package com.servlet.chapter;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Mr.PanYang on 2018/5/15.
 */
@WebServlet(name = "asyncServlet1", urlPatterns = "/async1", asyncSupported = true)
public class AsyncServlet1 extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Connection", "Keep-Alive");
        resp.setContentType("text/html;charset=utf-8");

        PrintWriter out = resp.getWriter();
        out.write("hello async");
        out.write("<br/>");
        out.flush();

        //1、开启异步
        final AsyncContext asyncContext = req.startAsync();
        asyncContext.setTimeout(5L * 1000);
        //这种方式的缺点就是可能和请求用同一个线程池，不推荐这种做法；从本质上讲和同步没啥区别（都要占用一个服务器线程）
        //不过如果请求压力较小，可以使用这种方法（因为有超时设置，可以防止一直不响应）
        asyncContext.start(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3L * 1000);
                    PrintWriter out = asyncContext.getResponse().getWriter();
                    out.write("over");
                    asyncContext.complete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

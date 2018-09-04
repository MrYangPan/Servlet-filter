package com.servlet.chapter;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Mr.PanYang on 2018/5/15.
 */
@WebServlet(name = "asyncServlet2", urlPatterns = "/async2", asyncSupported = true)
public class AsyncServlet2 extends HttpServlet {

    ExecutorService executorService = Executors.newScheduledThreadPool(2);

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
        //2、设置超时时间，如果不设置如jetty是30000L
        asyncContext.setTimeout(10L * 1000); //设置为0表示永不超时

        //把任务提交给自己的任务队列
        executorService.submit(new Runnable() {
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

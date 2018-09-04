package com.servlet.chapter.dispacher;

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
@WebServlet(name = "dispatchServlet1", urlPatterns = "/dispatch1", asyncSupported = true)
public class DispatchServlet1 extends HttpServlet {
    @Override
    protected void doGet(final HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getAttribute("ok") == null) {
            System.out.println("before start async:" + req.isAsyncStarted());
            final AsyncContext asyncContext = req.startAsync();
            System.out.println("after start async:" + req.isAsyncStarted());

            asyncContext.setTimeout(10L * 1000);
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

            //测试一
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Thread.sleep(3L * 1000);
//                    } catch (InterruptedException e) {
////                        e.printStackTrace();
//                    }
//                    req.setAttribute("ok", "true");
//                    req.setAttribute("msg", "success");
//                    asyncContext.dispatch();
//                    System.out.println("after dispatch before handle:" + req.isAsyncStarted());
//                }
//            }).start();

            //测试二
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3L * 1000);
                    } catch (InterruptedException e) {
                    }
                    req.setAttribute("ok", "true");
                    req.setAttribute("msg", "success");
                    asyncContext.dispatch("/WEB-INF/jsp/dispatch.jsp");
                }
            }).start();

//            return;
        } else {
            //此处通过输出可以看到调用不再是异步的了，即经过dispatch后，处理不是异步的了
            System.out.println("after dispatch in handling:" + req.isAsyncStarted());
            resp.getWriter().write((String) req.getAttribute("msg"));
        }

    }
}

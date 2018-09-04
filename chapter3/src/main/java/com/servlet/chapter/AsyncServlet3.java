package com.servlet.chapter;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Mr.PanYang on 2018/5/15.
 */
@WebServlet(name = "asyncServlet3", urlPatterns = "/async3", asyncSupported = true)
public class AsyncServlet3 extends HttpServlet {

    private final Queue<AsyncContext> queue = new ConcurrentLinkedQueue();

    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //模拟推送消息，主要实现方式两种方式：长轮询 + iframe长连接，具体自己搜索，本系列不介绍这些内容
                while (true) {
                    try {
                        //一秒执行一次
                        Thread.sleep(5L * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //注意这种方式不能用于实际环境，因为没有考虑过期情况 所以每刷新一次页面会创建一个新的AsyncContext
                    Iterator<AsyncContext> iter = queue.iterator();
                    while (iter.hasNext()) {
                        AsyncContext asyncContext = iter.next();
                        try {
                            ServletResponse response = asyncContext.getResponse();
                            PrintWriter out = response.getWriter();
                            String msg = "new message : " + System.currentTimeMillis();
//                            out.write("<script>parent.callback('" + msg + "');</script>");
                            out.write(msg + "<br>");
                            try {
                                response.flushBuffer();
                            } catch (IOException e) {
                                //远程主机可能强制关闭一个链接 直接把连接移除
                                iter.remove();
                                asyncContext.complete();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("push message count : " + queue.size());
                }
            }
        }).start();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Connection", "Keep-Alive");
        resp.addHeader("Cache-Control", "private");
        resp.addHeader("Pragma", "no-cache");
        resp.setContentType("text/html;charset=utf-8");
        resp.flushBuffer();

        //1、开启异步
        final AsyncContext asyncContext = req.startAsync();
        asyncContext.addListener(new AsyncListener() {
            @Override
            public void onComplete(final AsyncEvent event) throws IOException {
                queue.remove(event.getAsyncContext());
            }

            @Override
            public void onTimeout(final AsyncEvent event) throws IOException {
                event.getAsyncContext().complete();
                queue.remove(event.getAsyncContext());
            }

            @Override
            public void onError(final AsyncEvent event) throws IOException {
                queue.remove(event.getAsyncContext());
            }

            @Override
            public void onStartAsync(final AsyncEvent event) throws IOException {
            }
        });
        //将异步上下文加入到队列中，这样在未来可以推送消息
        queue.add(asyncContext);
    }

}

package com.servlet.chapter.chart;

import com.sun.javafx.util.Utils;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by Mr.PanYang on 2018/5/15.
 */
public class MsgPublisher {

    private volatile Map<String, Queue<AsyncContext>> usernameToAsyncContextMap = new ConcurrentHashMap();
    private static MsgPublisher instance = new MsgPublisher();

    public MsgPublisher() {
    }

    public static MsgPublisher getInstance() {
        return instance;
    }

    public Collection<String> getLoginUsers() {
        return new HashSet(usernameToAsyncContextMap.keySet());
    }

    public void startAsync(HttpServletRequest req, final String username) {
        //开启异步
        final AsyncContext asyncContext = req.startAsync();
        asyncContext.setTimeout(30L * 1000);
        //将异步上下文加入到队列中，这样在未来可以推送消息
        Queue<AsyncContext> queue = usernameToAsyncContextMap.get(username);
        if (queue == null || queue.size() == 0) {
            queue = new ConcurrentLinkedDeque();
            usernameToAsyncContextMap.put(username, queue);
        }
        queue.add(asyncContext);
        asyncContext.addListener(new AsyncListener() {
            @Override
            public void onComplete(AsyncEvent event) throws IOException {
                Queue<AsyncContext> queue = usernameToAsyncContextMap.get(username);
                if (queue != null) {
                    queue.remove(event.getAsyncContext());
                }
            }

            @Override
            public void onTimeout(AsyncEvent event) throws IOException {
                event.getAsyncContext().complete();
            }

            @Override
            public void onError(AsyncEvent event) throws IOException {
                Queue<AsyncContext> queue = usernameToAsyncContextMap.get(username);
                if (queue != null) {
                    queue.remove(event.getAsyncContext());
                }
            }

            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {

            }
        });
    }

    public void login(String username) {
        if (username != null && !usernameToAsyncContextMap.containsKey(username)) {
            StringBuilder data = new StringBuilder();
            data.append("{");
            data.append("\"type\" : \"login\"");
            data.append(",\"username\" : \"" + username + "\"");
            data.append("}");
            //用户加入到队列
            usernameToAsyncContextMap.put(username, new ConcurrentLinkedDeque());
            //查看并接收消息
            publish(null, username, data.toString());
        }
    }

    public void logout(String username) {
        if (username == null) {
            return;
        }
        Queue<AsyncContext> queue = usernameToAsyncContextMap.get(username);
        if (queue != null && queue.size() == 0) {
            StringBuilder data = new StringBuilder();
            data.append("{");
            data.append("\"type\" : \"logout\"");
            data.append(",\"username\" : \"" + username + "\"");
            data.append("}");
            publish(null, username, data.toString());
            usernameToAsyncContextMap.remove(username);
        }
    }

    public void send(String receiver, String sender, String msg) {
        StringBuilder data = new StringBuilder();
        data.append("{");
        data.append("\"type\" : \"msg\"");
        data.append(",\"username\" : \"" + sender + "\"");
        data.append(",\"msg\" : \"" + msg + "\"");
        data.append("}");
        publish(receiver, sender, data.toString());
    }

    /**
     * @Author: My.PanYang
     * @Description: 如果为空 表示发送给所有人
     * @Date: ` 2018/5/15
     */
    private void publish(String receiver, String sender, String data) {
        if (receiver == null || receiver.trim().length() == 0) {
            //发送给所有人
            for (String loginUsername : usernameToAsyncContextMap.keySet()) {
                if (loginUsername.equals(sender)) {
                    continue;
                }
                Queue<AsyncContext> queue = usernameToAsyncContextMap.get(loginUsername);
                IteratorQueue(queue, data);
            }
        } else {
            //私人消息
            Queue<AsyncContext> queue = usernameToAsyncContextMap.get(sender);
            IteratorQueue(queue, data);
        }
    }

    private void IteratorQueue(Queue<AsyncContext> queue, String data) {
        if (queue != null) {
            Iterator<AsyncContext> item = queue.iterator();
            while (item.hasNext()) {
                AsyncContext asyncContext = item.next();
                try {
                    if (asyncContext != null) {
                        ServletResponse response = asyncContext.getResponse();
                        PrintWriter out = response.getWriter();
                        out.write(data);
                        out.flush();
                        asyncContext.complete();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

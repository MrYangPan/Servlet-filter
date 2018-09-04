package com.servlet.chapter.chart;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by Mr.PanYang on 2018/5/16.
 */
@WebListener(value = "sessionListener")
public class SessionListener implements HttpSessionListener {

    private MsgPublisher msgPublisher = MsgPublisher.getInstance();

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        se.getSession().setMaxInactiveInterval(50);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        msgPublisher.logout(se.getSession().getAttribute("username").toString());
    }
}

package com.servlet.chapter.chart;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by Mr.PanYang on 2018/5/15.
 */
@WebServlet(name = "chatServlet", urlPatterns = "/chat", asyncSupported = true)
public class ChatServlet extends HttpServlet {

    private Configuration config;
    private MsgPublisher msgPublisher = MsgPublisher.getInstance();

    @Override
    public void init() throws ServletException {
        config = new Configuration();
        config.setServletContextForTemplateLoading(getServletContext(), "/WEB-INF/ftl");
        config.setDefaultEncoding("utf-8");
        config.setOutputEncoding("utf-8");
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        String command = req.getParameter("command");
        if ("login".equals(command)) {
            login(req, resp);
            return;
        } else if ("logout".equals(command)) {
            return;
        } else if ("send".equals(command)) {
            send(req, resp);
            return;
        } else if ("poll".equals(command)) {
            poll(req, resp);
            return;
        } else {
            //未知命令 忽略
        }
    }

    private void login(final HttpServletRequest req, final HttpServletResponse reps) throws ServletException, IOException {
        String username = req.getParameter("username");
        HttpSession session = req.getSession();
        session.setAttribute("username", username);
        msgPublisher.login(username);
        Set<String> loginUsers = (Set<String>) msgPublisher.getLoginUsers();
//        loginUsers.remove(username);
        req.setAttribute("loginUsers", loginUsers);
//        reps.setContentType("text/html;charset=utf-8");
        reps.setCharacterEncoding("utf-8");
        String url = req.getRequestURL().toString();
        //绑定数据
        Map<Object, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("users", loginUsers);
        map.put("url", url);
        PrintWriter writer = reps.getWriter();
        //整合数据模板
        Template template = config.getTemplate("/chatOnLine.ftl");
        try {
            template.process(map, writer);
        } catch (TemplateException e) {
            e.printStackTrace();
        }
//        req.getRequestDispatcher("/WEB-INF/chatOnLine.ftl").forward(req, reps);
    }

    private void send(final HttpServletRequest req, final HttpServletResponse reps) {
//        String username = (String) req.getSession().getAttribute("username");
        String username = req.getParameter("username");
        String receiver = req.getParameter("receiver");
        String msg = req.getParameter("msg");
        msgPublisher.send(receiver, username, msg);
    }

    private void poll(final HttpServletRequest req, final HttpServletResponse resp) {
        resp.setHeader("Connection", "Keep-Alive");
        resp.addHeader("Cache-Control", "private");
        resp.addHeader("Pragma", "no-cache");
        resp.setContentType("text/html;charset=utf-8");

        String username = req.getParameter("username");
        if (username == null) {
            username = (String) req.getSession().getAttribute("username");
        }

        if (username != null) {
            msgPublisher.startAsync(req, username);
        }
    }

}

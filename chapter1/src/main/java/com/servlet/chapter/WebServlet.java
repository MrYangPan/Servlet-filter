package com.servlet.chapter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Mr.PanYang on 2018/5/11.
 */
@javax.servlet.annotation.WebServlet(name = "webServlet", urlPatterns = {"/s1", "/s1/*"}, loadOnStartup = 1, initParams = {@WebInitParam(name = "msg", value = "hello dispacher")})
public class WebServlet extends HttpServlet {
    private String msg;

    public WebServlet() {
        System.out.println("load on startup");
    }

    @Override
    public void init() throws ServletException {
        super.init();
        msg = this.getInitParameter("msg");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("doGet:  " + msg);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("doPost:  " + msg);
    }

    @Override
    public void destroy() {
        System.out.println("WebServlet 执行 destroy");
    }
}

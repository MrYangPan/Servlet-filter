package com.servlet.chapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Mr.PanYang on 2018/5/11.
 */
@javax.servlet.annotation.WebServlet(name = "WebService", urlPatterns = "/s2")
public class WebServlet extends HttpServlet {
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("WebServlet  request");
    }

    @Override
    public void init() throws ServletException {
        System.out.println("WebServlet  init");
    }
}

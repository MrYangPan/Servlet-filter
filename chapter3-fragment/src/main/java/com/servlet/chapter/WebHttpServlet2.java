package com.servlet.chapter;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Mr.PanYang on 2018/5/14.
 */
@WebServlet(name = "fragment2", urlPatterns = "/fragment2")
public class WebHttpServlet2 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("WebHttpServlet  dispacher request");
    }
}

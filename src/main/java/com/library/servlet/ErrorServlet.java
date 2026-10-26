package com.library.servlet;

import com.library.util.HtmlUtil;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/** Shows a friendly page instead of Tomcat's default error page (no stack traces). */
@WebServlet("/error")
public class ErrorServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Object code = req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String message = (code != null && "404".equals(code.toString()))
                ? "The page you requested was not found."
                : "Something went wrong. Please go back and try again.";

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        String ctx = req.getContextPath();
        HtmlUtil.header(out, ctx, "Error");
        HtmlUtil.alert(out, "error", message);
        out.println("<p><a class=\"btn btn-primary\" href=\"" + HtmlUtil.esc(ctx) + "/\">Back to Home</a></p>");
        HtmlUtil.footer(out);
    }
}
package com.library.servlet;

import com.library.dao.BookDAO;
import com.library.model.Book;
import com.library.util.BookValidator;
import com.library.util.HtmlUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@WebServlet("/addBook")
public class AddBookServlet extends HttpServlet {

    private final BookDAO dao = new BookDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        render(req, resp, 200, Collections.emptyList(), "", "", "", "");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String bname = BookValidator.clean(req.getParameter("bname"));
        String author = BookValidator.clean(req.getParameter("author"));
        String btype = BookValidator.clean(req.getParameter("btype"));
        String price = BookValidator.clean(req.getParameter("price"));

        List<String> errors = BookValidator.validate(bname, author, btype, price);
        if (!errors.isEmpty()) {
            render(req, resp, 400, errors, bname, author, btype, price);
            return;
        }

        try {
            dao.addBook(new Book(0, bname, author, btype, Float.parseFloat(price)));
            // Redirect after POST: refreshing the page will not insert the book twice
            resp.sendRedirect(req.getContextPath() + "/viewBooks?msg=added");
        } catch (SQLException e) {
            log("Database error while adding a book", e);
            render(req, resp, 500, List.of(HtmlUtil.DB_ERROR), bname, author, btype, price);
        }
    }

    private void render(HttpServletRequest req, HttpServletResponse resp, int status,
                        List<String> errors, String bname, String author, String btype, String price)
            throws IOException {
        resp.setStatus(status);
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        String ctx = req.getContextPath();
        HtmlUtil.header(out, ctx, "Add Book");
        HtmlUtil.errors(out, errors);
        HtmlUtil.bookForm(out, ctx, "/addBook", "Add Book", null, bname, author, btype, price);
        HtmlUtil.footer(out);
    }
}
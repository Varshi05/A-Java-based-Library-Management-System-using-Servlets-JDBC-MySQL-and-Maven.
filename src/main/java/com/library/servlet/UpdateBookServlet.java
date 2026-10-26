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

@WebServlet("/updateBook")
public class UpdateBookServlet extends HttpServlet {

    private final BookDAO dao = new BookDAO();

    // Step 1: choose an ID, or show the prefilled form for ?id=...
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idParam = BookValidator.clean(req.getParameter("id"));
        if (idParam.isEmpty()) {
            showIdForm(req, resp, 200, Collections.emptyList());
            return;
        }

        Integer id = BookValidator.parseId(idParam);
        if (id == null) {
            showIdForm(req, resp, 400, List.of("Book ID must be a positive whole number."));
            return;
        }

        try {
            Book b = dao.getBookById(id);
            if (b == null) {
                showIdForm(req, resp, 404, List.of("No book found with ID " + id + "."));
                return;
            }
            showForm(req, resp, 200, Collections.emptyList(), id,
                    b.getBname(), b.getAuthor(), b.getBtype(), HtmlUtil.formatPrice(b.getPrice()));
        } catch (SQLException e) {
            log("Database error while loading book " + id, e);
            showIdForm(req, resp, 500, List.of(HtmlUtil.DB_ERROR));
        }
    }

    // Step 2: save the changes
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        Integer id = BookValidator.parseId(req.getParameter("id"));
        if (id == null) {
            showIdForm(req, resp, 400, List.of("A valid book ID is required."));
            return;
        }

        String bname = BookValidator.clean(req.getParameter("bname"));
        String author = BookValidator.clean(req.getParameter("author"));
        String btype = BookValidator.clean(req.getParameter("btype"));
        String price = BookValidator.clean(req.getParameter("price"));

        List<String> errors = BookValidator.validate(bname, author, btype, price);
        if (!errors.isEmpty()) {
            showForm(req, resp, 400, errors, id, bname, author, btype, price);
            return;
        }

        try {
            boolean updated = dao.updateBook(new Book(id, bname, author, btype, Float.parseFloat(price)));
            resp.sendRedirect(req.getContextPath() + "/viewBooks?msg=" + (updated ? "updated" : "notfound"));
        } catch (SQLException e) {
            log("Database error while updating book " + id, e);
            showForm(req, resp, 500, List.of(HtmlUtil.DB_ERROR), id, bname, author, btype, price);
        }
    }

    private void showIdForm(HttpServletRequest req, HttpServletResponse resp, int status,
                            List<String> errors) throws IOException {
        resp.setStatus(status);
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        String ctx = req.getContextPath();
        HtmlUtil.header(out, ctx, "Update Book");
        HtmlUtil.errors(out, errors);
        out.println("<p class=\"hint\">Enter the ID of the book you want to update (you can find it in View Books).</p>");
        HtmlUtil.idForm(out, ctx, "/updateBook", "Find Book");
        HtmlUtil.footer(out);
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp, int status,
                          List<String> errors, Integer id, String bname, String author,
                          String btype, String price) throws IOException {
        resp.setStatus(status);
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        String ctx = req.getContextPath();
        HtmlUtil.header(out, ctx, "Update Book");
        HtmlUtil.errors(out, errors);
        out.println("<p class=\"hint\">Editing book ID " + id + "</p>");
        HtmlUtil.bookForm(out, ctx, "/updateBook", "Save Changes", id, bname, author, btype, price);
        HtmlUtil.footer(out);
    }
}
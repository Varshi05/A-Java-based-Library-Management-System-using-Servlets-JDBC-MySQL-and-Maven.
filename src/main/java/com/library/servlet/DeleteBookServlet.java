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

@WebServlet("/deleteBook")
public class DeleteBookServlet extends HttpServlet {

    private final BookDAO dao = new BookDAO();

    // Step 1: choose an ID, or show the confirmation page for ?id=...
    // GET never deletes anything.
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
            showConfirm(req, resp, b);
        } catch (SQLException e) {
            log("Database error while loading book " + id, e);
            showIdForm(req, resp, 500, List.of(HtmlUtil.DB_ERROR));
        }
    }

    // Step 2: the user confirmed, so delete (POST because it changes data)
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        Integer id = BookValidator.parseId(req.getParameter("id"));
        if (id == null) {
            showIdForm(req, resp, 400, List.of("A valid book ID is required."));
            return;
        }

        try {
            boolean deleted = dao.deleteBook(id);
            resp.sendRedirect(req.getContextPath() + "/viewBooks?msg=" + (deleted ? "deleted" : "notfound"));
        } catch (SQLException e) {
            log("Database error while deleting book " + id, e);
            showIdForm(req, resp, 500, List.of(HtmlUtil.DB_ERROR));
        }
    }

    private void showIdForm(HttpServletRequest req, HttpServletResponse resp, int status,
                            List<String> errors) throws IOException {
        resp.setStatus(status);
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        String ctx = req.getContextPath();
        HtmlUtil.header(out, ctx, "Delete Book");
        HtmlUtil.errors(out, errors);
        out.println("<p class=\"hint\">Enter the ID of the book you want to delete (you can find it in View Books).</p>");
        HtmlUtil.idForm(out, ctx, "/deleteBook", "Find Book");
        HtmlUtil.footer(out);
    }

    private void showConfirm(HttpServletRequest req, HttpServletResponse resp, Book b)
            throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        String ctx = HtmlUtil.esc(req.getContextPath());
        HtmlUtil.header(out, req.getContextPath(), "Delete Book");
        HtmlUtil.alert(out, "warning", "Are you sure you want to delete this book? This cannot be undone.");
        out.println("<div class=\"card\">");
        out.println("  <dl class=\"details\">");
        out.println("    <dt>ID</dt><dd>" + b.getId() + "</dd>");
        out.println("    <dt>Book Name</dt><dd>" + HtmlUtil.esc(b.getBname()) + "</dd>");
        out.println("    <dt>Author</dt><dd>" + HtmlUtil.esc(b.getAuthor()) + "</dd>");
        out.println("    <dt>Book Type</dt><dd>" + HtmlUtil.esc(b.getBtype()) + "</dd>");
        out.println("    <dt>Price</dt><dd>" + HtmlUtil.formatPrice(b.getPrice()) + "</dd>");
        out.println("  </dl>");
        out.println("  <form method=\"post\" action=\"" + ctx + "/deleteBook\">");
        out.println("    <input type=\"hidden\" name=\"id\" value=\"" + b.getId() + "\">");
        out.println("    <div class=\"actions\">");
        out.println("      <button type=\"submit\" class=\"btn btn-danger\">Yes, Delete</button>");
        out.println("      <a class=\"btn btn-secondary\" href=\"" + ctx + "/viewBooks\">Cancel</a>");
        out.println("    </div>");
        out.println("  </form>");
        out.println("</div>");
        HtmlUtil.footer(out);
    }
}
package com.library.servlet;

import com.library.dao.BookDAO;
import com.library.model.Book;
import com.library.util.HtmlUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/viewBooks")
public class ViewBooksServlet extends HttpServlet {

    private final BookDAO dao = new BookDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Book> books = null;
        try {
            books = dao.getAllBooks();
        } catch (SQLException e) {
            log("Database error while listing books", e);
            resp.setStatus(500);
        }

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        String ctx = req.getContextPath();

        HtmlUtil.header(out, ctx, "View Books");

        String msg = req.getParameter("msg");
        if ("added".equals(msg)) {
            HtmlUtil.alert(out, "success", "Book added successfully.");
        } else if ("updated".equals(msg)) {
            HtmlUtil.alert(out, "success", "Book updated successfully.");
        } else if ("deleted".equals(msg)) {
            HtmlUtil.alert(out, "success", "Book deleted successfully.");
        } else if ("notfound".equals(msg)) {
            HtmlUtil.alert(out, "error", "That book no longer exists. Nothing was changed.");
        }

        if (books == null) {
            HtmlUtil.alert(out, "error", HtmlUtil.DB_ERROR);
        } else if (books.isEmpty()) {
            out.println("<div class=\"card empty\">");
            out.println("  <p>No books found.</p>");
            out.println("  <a class=\"btn btn-primary\" href=\"" + HtmlUtil.esc(ctx) + "/addBook\">Add your first book</a>");
            out.println("</div>");
        } else {
            out.println("<div class=\"table-wrap\">");
            out.println("<table>");
            out.println("<thead><tr><th>ID</th><th>Book Name</th><th>Author</th><th>Book Type</th><th>Price</th><th>Actions</th></tr></thead>");
            out.println("<tbody>");
            for (Book b : books) {
                out.println("<tr>");
                out.println("<td>" + b.getId() + "</td>");
                out.println("<td>" + HtmlUtil.esc(b.getBname()) + "</td>");
                out.println("<td>" + HtmlUtil.esc(b.getAuthor()) + "</td>");
                out.println("<td>" + HtmlUtil.esc(b.getBtype()) + "</td>");
                out.println("<td>" + HtmlUtil.formatPrice(b.getPrice()) + "</td>");
                out.println("<td class=\"row-actions\">"
                        + "<a class=\"btn btn-small btn-primary\" href=\"" + HtmlUtil.esc(ctx) + "/updateBook?id=" + b.getId() + "\">Update</a> "
                        + "<a class=\"btn btn-small btn-danger\" href=\"" + HtmlUtil.esc(ctx) + "/deleteBook?id=" + b.getId() + "\">Delete</a>"
                        + "</td>");
                out.println("</tr>");
            }
            out.println("</tbody>");
            out.println("</table>");
            out.println("</div>");
        }

        HtmlUtil.footer(out);
    }
}
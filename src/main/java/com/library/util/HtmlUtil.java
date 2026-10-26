package com.library.util;

import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;

public final class HtmlUtil {

    public static final String DB_ERROR =
            "A database error occurred. Please try again later.";

    private HtmlUtil() { }

    /** Escapes text so user input cannot inject HTML or scripts. */
    public static String esc(String s) {
        if (s == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(s.length() + 16);
        for (char c : s.toCharArray()) {
            switch (c) {
                case '&':  sb.append("&amp;");  break;
                case '<':  sb.append("&lt;");   break;
                case '>':  sb.append("&gt;");   break;
                case '"':  sb.append("&quot;"); break;
                case '\'': sb.append("&#39;");  break;
                default:   sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String formatPrice(float price) {
        return String.format(Locale.ROOT, "%.2f", price);
    }

    public static void header(PrintWriter out, String ctx, String title) {
        String c = esc(ctx);
        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"en\">");
        out.println("<head>");
        out.println("  <meta charset=\"UTF-8\">");
        out.println("  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
        out.println("  <title>" + esc(title) + " | Library Management System</title>");
        out.println("  <link rel=\"stylesheet\" href=\"" + c + "/css/style.css\">");
        out.println("</head>");
        out.println("<body>");
        out.println("<header class=\"site-header\">");
        out.println("  <a class=\"brand\" href=\"" + c + "/\">Library Management System</a>");
        out.println("  <nav>");
        out.println("    <a href=\"" + c + "/addBook\">Add Book</a>");
        out.println("    <a href=\"" + c + "/viewBooks\">View Books</a>");
        out.println("    <a href=\"" + c + "/updateBook\">Update Book</a>");
        out.println("    <a href=\"" + c + "/deleteBook\">Delete Book</a>");
        out.println("  </nav>");
        out.println("</header>");
        out.println("<main class=\"container\">");
        out.println("<h1>" + esc(title) + "</h1>");
    }

    public static void footer(PrintWriter out) {
        out.println("</main>");
        out.println("<footer class=\"site-footer\">Library Management System &middot; Servlets &middot; JDBC &middot; MySQL</footer>");
        out.println("</body>");
        out.println("</html>");
    }

    /** type is one of: success, error, warning. */
    public static void alert(PrintWriter out, String type, String message) {
        out.println("<div class=\"alert alert-" + type + "\">" + esc(message) + "</div>");
    }

    public static void errors(PrintWriter out, List<String> errors) {
        if (errors == null || errors.isEmpty()) {
            return;
        }
        if (errors.size() == 1) {
            alert(out, "error", errors.get(0));
            return;
        }
        out.println("<div class=\"alert alert-error\"><ul>");
        for (String e : errors) {
            out.println("<li>" + esc(e) + "</li>");
        }
        out.println("</ul></div>");
    }

    /** Small form asking for a book ID (used by Update and Delete). */
    public static void idForm(PrintWriter out, String ctx, String servletPath, String button) {
        out.println("<form class=\"card form\" method=\"get\" action=\"" + esc(ctx + servletPath) + "\">");
        out.println("  <div class=\"field\">");
        out.println("    <label for=\"id\">Book ID</label>");
        out.println("    <input id=\"id\" name=\"id\" type=\"number\" min=\"1\" required>");
        out.println("  </div>");
        out.println("  <div class=\"actions\"><button type=\"submit\" class=\"btn btn-primary\">" + esc(button) + "</button></div>");
        out.println("</form>");
    }

    /** Add / Update form. Pass id = null for Add. */
    public static void bookForm(PrintWriter out, String ctx, String servletPath, String button,
                                Integer id, String bname, String author, String btype, String price) {
        out.println("<form class=\"card form\" method=\"post\" action=\"" + esc(ctx + servletPath) + "\">");
        if (id != null) {
            out.println("  <input type=\"hidden\" name=\"id\" value=\"" + id + "\">");
        }
        field(out, "bname", "Book Name", "text", "maxlength=\"20\"", bname);
        field(out, "author", "Author", "text", "maxlength=\"20\"", author);
        field(out, "btype", "Book Type", "text", "maxlength=\"20\"", btype);
        field(out, "price", "Price", "number", "step=\"0.01\" min=\"0\"", price);
        out.println("  <div class=\"actions\">");
        out.println("    <button type=\"submit\" class=\"btn btn-primary\">" + esc(button) + "</button>");
        out.println("    <a class=\"btn btn-secondary\" href=\"" + esc(ctx) + "/viewBooks\">Cancel</a>");
        out.println("  </div>");
        out.println("</form>");
    }

    private static void field(PrintWriter out, String name, String label, String type,
                              String extraAttrs, String value) {
        out.println("  <div class=\"field\">");
        out.println("    <label for=\"" + name + "\">" + label + "</label>");
        out.println("    <input id=\"" + name + "\" name=\"" + name + "\" type=\"" + type + "\" "
                + extraAttrs + " required value=\"" + esc(value) + "\">");
        out.println("  </div>");
    }
}
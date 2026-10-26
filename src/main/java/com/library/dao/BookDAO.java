package com.library.dao;

import com.library.model.Book;
import com.library.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public void addBook(Book b) throws SQLException {
        String sql = "INSERT INTO books (bname, author, btype, price) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, b.getBname());
            ps.setString(2, b.getAuthor());
            ps.setString(3, b.getBtype());
            ps.setFloat(4, b.getPrice());
            ps.executeUpdate();
        }
    }

    public List<Book> getAllBooks() throws SQLException {
        String sql = "SELECT id, bname, author, btype, price FROM books ORDER BY id";
        List<Book> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    /** Returns the book, or null if no book has this id. */
    public Book getBookById(int id) throws SQLException {
        String sql = "SELECT id, bname, author, btype, price FROM books WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    /** Returns true if a row was updated, false if the id does not exist. */
    public boolean updateBook(Book b) throws SQLException {
        String sql = "UPDATE books SET bname = ?, author = ?, btype = ?, price = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, b.getBname());
            ps.setString(2, b.getAuthor());
            ps.setString(3, b.getBtype());
            ps.setFloat(4, b.getPrice());
            ps.setInt(5, b.getId());
            return ps.executeUpdate() > 0;
        }
    }

    /** Returns true if a row was deleted, false if the id does not exist. */
    public boolean deleteBook(int id) throws SQLException {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Book mapRow(ResultSet rs) throws SQLException {
        return new Book(
                rs.getInt("id"),
                rs.getString("bname"),
                rs.getString("author"),
                rs.getString("btype"),
                rs.getFloat("price"));
    }
}
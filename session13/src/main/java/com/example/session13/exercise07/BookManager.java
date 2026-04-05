package com.example.session13.exercise07;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookManager {
    // --- CÁC HÀM NGHIỆP VỤ CHÍNH ---

    public boolean addBook(Book book) {
        if (isBookExists(book.getTitle(), book.getAuthor())) {
            return false; // Trả về false nếu đã tồn tại
        }

        String sql = "INSERT INTO books (title, author, published_year, price) VALUES (?, ?, ?, ?)";
        // try-with-resources tự động đóng Connection và PreparedStatement
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setInt(3, book.getPublishedYear());
            stmt.setBigDecimal(4, book.getPrice());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateBook(int id, Book book) {
        if (!isBookExistsById(id)) {
            return false;
        }

        String sql = "UPDATE Book SET title = ?, author = ?, published_year = ?, price = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setInt(3, book.getPublishedYear());
            stmt.setBigDecimal(4, book.getPrice());
            stmt.setInt(5, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBook(int id) {
        if (!isBookExistsById(id)) {
            return false;
        }

        String sql = "DELETE FROM Book WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Book> findBooksByAuthor(String author) {
        String sql = "SELECT * FROM books WHERE author LIKE ?";
        return executeQueryAndMapToList(sql, "%" + author + "%"); // Hỗ trợ tìm kiếm tương đối (LIKE)
    }

    public List<Book> listAllBooks() {
        String sql = "SELECT * FROM books";
        return executeQueryAndMapToList(sql, null);
    }

    // --- CÁC HÀM HELPER ĐỂ CODE CLEAN HƠN (DRY Principle) ---

    private boolean isBookExists(String title, String author) {
        String sql = "SELECT 1 FROM books WHERE title = ? AND author = ? LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, author);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isBookExistsById(int id) {
        String sql = "SELECT 1 FROM books WHERE id = ? LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Helper ánh xạ (mapping) ResultSet thành List<Book>.
     * Tương đương với quá trình Type Casting array object trả về từ query mysql2
     * trong NodeJS.
     */
    private List<Book> executeQueryAndMapToList(String sql, String param) {
        var books = new ArrayList<Book>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (param != null) {
                stmt.setString(1, param);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(new Book(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getInt("published_year"),
                            rs.getBigDecimal("price")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
}

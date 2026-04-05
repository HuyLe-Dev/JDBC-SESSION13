package com.example.session13.exercise05;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovieManagement {
    public void addMovie(String title, String diretor, int year) {
        try (Connection connection = DBConnection.getConnection();
                CallableStatement cs = connection.prepareCall("{call add_movie(?, ?, ?)}")) {
            cs.setString(1, title);
            cs.setString(2, diretor);
            cs.setInt(3, year);
            cs.executeUpdate();

            System.out.println("Thêm phim thành công!");
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm phim: " + e.getMessage());
        }
    }

    public List<Movie> listMovies() {
        List<Movie> results = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
                CallableStatement cs = connection.prepareCall("{call list_movies(?, ?, ?)}");
                ResultSet rs = cs.executeQuery();) {
            while (rs.next()) {
                results.add(
                        new Movie(rs.getInt("id"), rs.getString("title"), rs.getString("director"), rs.getInt("year")));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách phim: " + e.getMessage());
        }

        return results;
    }

    public void updateMovie(int id, String title, String director, int year) {
        try (Connection connection = DBConnection.getConnection();
                CallableStatement cs = connection.prepareCall("{call update_movie(?, ?, ?, ?)}")) {
            cs.setInt(1, id);
            cs.setString(2, title);
            cs.setString(3, director);
            cs.setInt(4, year);

            int affected = cs.executeUpdate();
            if (affected == 0) {
                System.out.println("Không tìm thấy phim với ID: " + id);
            } else {
                System.out.println("Cập nhật phim thành công!");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật phim: " + e.getMessage());
        }
    }

    public void deleteMovie(int id) {
        try (Connection conn = DBConnection.getConnection();
                CallableStatement cs = conn.prepareCall("{call delete_movie(?)}")) {

            cs.setInt(1, id);

            int affected = cs.executeUpdate();
            if (affected == 0) {
                System.out.println("Không tìm thấy phim với ID: " + id);
            } else {
                System.out.println("Xóa phim thành công!");
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa phim: " + e.getMessage());
        }
    }
}

-- =============================================
-- SCHEMA
-- =============================================
CREATE DATABASE IF NOT EXISTS school_db;
USE school_db;

CREATE TABLE IF NOT EXISTS Students (
    student_id  INT          AUTO_INCREMENT PRIMARY KEY,
    full_name   VARCHAR(100) NOT NULL,
    date_of_birth DATE       NOT NULL,
    email       VARCHAR(100) NOT NULL UNIQUE
);

-- =============================================
-- STORED PROCEDURES
-- =============================================

DELIMITER //

-- 1. Lấy tất cả sinh viên
CREATE PROCEDURE get_all_students()
BEGIN
    SELECT student_id, full_name, date_of_birth, email
    FROM Students
    ORDER BY student_id;
END //

-- 2. Thêm sinh viên mới
CREATE PROCEDURE add_student(
    IN in_full_name    VARCHAR(100),
    IN in_date_of_birth DATE,
    IN in_email        VARCHAR(100)
)
BEGIN
    INSERT INTO Students(full_name, date_of_birth, email)
    VALUES (in_full_name, in_date_of_birth, in_email);
END //

-- 3. Cập nhật thông tin sinh viên
CREATE PROCEDURE update_student(
    IN in_id           INT,
    IN in_full_name    VARCHAR(100),
    IN in_date_of_birth DATE,
    IN in_email        VARCHAR(100)
)
BEGIN
    UPDATE Students
    SET full_name     = in_full_name,
        date_of_birth = in_date_of_birth,
        email         = in_email
    WHERE student_id  = in_id;
END //

-- 4. Tìm sinh viên theo ID
CREATE PROCEDURE find_student_by_id(IN in_id INT)
BEGIN
    SELECT student_id, full_name, date_of_birth, email
    FROM Students
    WHERE student_id = in_id;
END //

-- 5. Xóa sinh viên theo ID
CREATE PROCEDURE delete_student(IN in_id INT)
BEGIN
    DELETE FROM Students
    WHERE student_id = in_id;
END //

DELIMITER ;

-- =============================================
-- TEST (chạy từng lệnh để kiểm tra)
-- =============================================

-- Thêm dữ liệu mẫu
CALL add_student('Nguyen Van A', '2000-01-15', 'vana@email.com');
CALL add_student('Tran Thi B',   '2001-05-20', 'thib@email.com');
CALL add_student('Le Minh C',    '1999-11-30', 'minhc@email.com');

-- Lấy tất cả
CALL get_all_students();

-- Tìm theo ID
CALL find_student_by_id(1);

-- Cập nhật
CALL update_student(1, 'Nguyen Van A (updated)', '2000-01-15', 'vana_new@email.com');

-- Kiểm tra sau update
CALL find_student_by_id(1);

-- Xóa
CALL delete_student(2);

-- Kiểm tra sau xóa
CALL get_all_students();

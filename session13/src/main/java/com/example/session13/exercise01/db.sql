CREATE DATABASE IF NOT EXISTS student_db;
USE student_db;

CREATE TABLE students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    age INT
);

DELIMITER //
CREATE PROCEDURE sp_add_student(IN p_name VARCHAR(100), IN p_age INT)
BEGIN
    INSERT INTO students(name, age) VALUES (p_name, p_age);
END //
DELIMITER ;
USE student_db;

DELIMITER //
CREATE PROCEDURE search_student(IN in_name VARCHAR(100))
BEGIN
    SELECT id, name, age
    FROM students
    WHERE name LIKE CONCAT('%', in_name, '%');
END //
DELIMITER ;

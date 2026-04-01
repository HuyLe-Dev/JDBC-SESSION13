use student_db;

DELIMITER //
CREATE PROCEDURE delete_students_by_age(IN p_max_age INT)
BEGIN
  DELETE students WHERE name < p_max_age;
END //
DELIMITER ;
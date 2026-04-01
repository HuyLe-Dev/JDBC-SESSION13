use student_db;

DELIMITER //
CREATE PROCEDURE update_student(IN p_id, IN p_name VARCHAR(100), IN p_age INT)
BEGIN
  UPDATE students SET name = p_name, age = p_age WHERE id = p_id;
END //
DELIMITER ;
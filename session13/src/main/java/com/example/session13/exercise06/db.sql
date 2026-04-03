CREATE DATABASE IF NOT EXISTS todo_db;
USE todo_db;

CREATE TABLE tasks (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    task_name VARCHAR(200) NOT NULL,
    status    VARCHAR(20)  NOT NULL DEFAULT 'chưa hoàn thành'
);

DELIMITER //

CREATE PROCEDURE add_task(IN p_task_name VARCHAR(200), IN p_status VARCHAR(20))
BEGIN
    INSERT INTO tasks(task_name, status) VALUES (p_task_name, p_status);
END //

CREATE PROCEDURE list_tasks()
BEGIN
    SELECT id, task_name, status FROM tasks ORDER BY id;
END //

CREATE PROCEDURE update_task_status(IN p_id INT, IN p_status VARCHAR(20))
BEGIN
    UPDATE tasks SET status = p_status WHERE id = p_id;
END //

CREATE PROCEDURE delete_task(IN p_id INT)
BEGIN
    DELETE FROM tasks WHERE id = p_id;
END //

CREATE PROCEDURE search_task_by_name(IN p_task_name VARCHAR(200))
BEGIN
    SELECT id, task_name, status FROM tasks
    WHERE task_name LIKE CONCAT('%', p_task_name, '%');
END //

CREATE PROCEDURE task_statistics()
BEGIN
    SELECT
        SUM(status = 'đã hoàn thành')   AS completed,
        SUM(status = 'chưa hoàn thành') AS pending
    FROM tasks;
END //

DELIMITER ;

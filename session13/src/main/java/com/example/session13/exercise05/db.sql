CREATE DATABASE IF NOT EXISTS movie_db;
USE movie_db;

CREATE TABLE movies (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    title    VARCHAR(200) NOT NULL,
    director VARCHAR(100) NOT NULL,
    year     INT          NOT NULL
);

DELIMITER //

CREATE PROCEDURE add_movie(IN p_title VARCHAR(200), IN p_director VARCHAR(100), IN p_year INT)
BEGIN
    INSERT INTO movies(title, director, year) VALUES (p_title, p_director, p_year);
END //

CREATE PROCEDURE list_movies()
BEGIN
    SELECT id, title, director, year FROM movies ORDER BY id;
END //

CREATE PROCEDURE update_movie(IN p_id INT, IN p_title VARCHAR(200), IN p_director VARCHAR(100), IN p_year INT)
BEGIN
    UPDATE movies SET title = p_title, director = p_director, year = p_year WHERE id = p_id;
END //

CREATE PROCEDURE delete_movie(IN p_id INT)
BEGIN
    DELETE FROM movies WHERE id = p_id;
END //

DELIMITER ;

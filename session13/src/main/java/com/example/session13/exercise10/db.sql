CREATE DATABASE IF NOT EXISTS university_db;
USE university_db;

CREATE TABLE student (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE course (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) UNIQUE NOT NULL,
    credits INT NOT NULL
);

CREATE TABLE enrollment (
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    grade DECIMAL(5, 2), -- Có thể NULL
    PRIMARY KEY (student_id, course_id),
    FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE
);
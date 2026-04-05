CREATE DATABASE IF NOT EXISTS library_db;
USE library_db;

CREATE TABLE books (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    author      VARCHAR(255) NOT NULL,
    published_year INT NOT NULL,
    price       DECIMAL(10, 2) NOT NULL
);
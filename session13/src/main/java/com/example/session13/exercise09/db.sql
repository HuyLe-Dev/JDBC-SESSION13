CREATE DATABASE IF NOT EXISTS company_db;
USE company_db;

CREATE TABLE employee (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    department VARCHAR(255) NOT NULL,
    salary DECIMAL(10, 2) NOT NULL
);

CREATE TABLE project (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    budget DECIMAL(10, 2) NOT NULL
);

CREATE TABLE assignment (
    employee_id INT NOT NULL,
    project_id INT NOT NULL,
    role VARCHAR(255) NOT NULL,
    PRIMARY KEY (employee_id, project_id),
    FOREIGN KEY (employee_id) REFERENCES employee(id) ON DELETE CASCADE,
    FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE
);
CREATE DATABASE IF NOT EXISTS librarydb;
USE librarydb;

CREATE TABLE IF NOT EXISTS books (
    id     INT PRIMARY KEY AUTO_INCREMENT,
    bname  VARCHAR(20),
    author VARCHAR(20),
    btype  VARCHAR(20),
    price  FLOAT
);

-- Optional test data (run once):
-- INSERT INTO books (bname, author, btype, price) VALUES
--   ('Java Basics',  'James Gosling', 'Programming', 450.50),
--   ('The Alchemist', 'Paulo Coelho', 'Fiction',      299.00);

-- Verify:
-- SHOW TABLES;
-- DESCRIBE books;
-- SELECT * FROM books;
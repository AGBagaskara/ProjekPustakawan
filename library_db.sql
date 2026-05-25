-- ============================================================
--  Library Management System - Database Schema
-- ============================================================

CREATE DATABASE IF NOT EXISTS library_db;
USE library_db;

-- --------------------------------------------------------
-- Tabel buku
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `book` (
  `id`       INT(11)      NOT NULL AUTO_INCREMENT,
  `title`    VARCHAR(255) NOT NULL,
  `author`   VARCHAR(255) NOT NULL,
  `isbn`     VARCHAR(50)  NOT NULL UNIQUE,
  `stock`    INT(11)      NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------
-- Tabel peminjaman
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `loan` (
  `id`            INT(11)      NOT NULL AUTO_INCREMENT,
  `borrower_name` VARCHAR(255) NOT NULL,
  `book_id`       INT(11)      NOT NULL,
  `loan_date`     DATE         NOT NULL,
  `due_date`      DATE         NOT NULL,
  `return_date`   DATE         DEFAULT NULL,
  `status`        VARCHAR(20)  NOT NULL DEFAULT 'DIPINJAM',
  PRIMARY KEY (`id`),
  FOREIGN KEY (`book_id`) REFERENCES `book`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------
-- Sample data
-- --------------------------------------------------------
INSERT INTO `book` (title, author, isbn, stock) VALUES
  ('Clean Code',                   'Robert C. Martin', '978-0132350884', 3),
  ('The Pragmatic Programmer',     'David Thomas',     '978-0135957059', 2),
  ('Design Patterns',              'Gang of Four',     '978-0201633610', 1),
  ('Introduction to Algorithms',  'Thomas H. Cormen', '978-0262033848', 2),
  ('Head First Java',              'Kathy Sierra',     '978-0596009205', 4);

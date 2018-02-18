USE algorithms;
SHOW TABLES;

CREATE TABLE design_paradigm (
  paradigm_id INT(10)     NOT NULL AUTO_INCREMENT PRIMARY KEY,
  paradigm    VARCHAR(50) NOT NULL
);
CREATE TABLE field_of_study (
  field_id    INT(10)     NOT NULL AUTO_INCREMENT,
  name        VARCHAR(50) NOT NULL,
  description VARCHAR(255)         DEFAULT NULL,
  PRIMARY KEY (field_id)
);
CREATE TABLE implementation_type (
  implementation_id INT(10)      NOT NULL AUTO_INCREMENT PRIMARY KEY,
  type              VARCHAR(100) NOT NULL
);
CREATE TABLE algorithm (
  algorithm_id           INT(10)      NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name                   VARCHAR(100) NOT NULL,
  complexity             VARCHAR(60)  NOT NULL,
  algo_paradigm_id       INT(10)      NOT NULL,
  algo_implementation_id INT(10)      NOT NULL,
  FOREIGN KEY (algo_paradigm_id)
  REFERENCES design_paradigm (paradigm_id),
  FOREIGN KEY (algo_implementation_id)
  REFERENCES implementation_type (implementation_id)
);
CREATE TABLE field (
  field_id    INT(10)     NOT NULL AUTO_INCREMENT,
  name        VARCHAR(50) NOT NULL,
  description VARCHAR(255)         DEFAULT NULL,
  PRIMARY KEY (field_id)
);
CREATE TABLE algorithm_application (
  application_id   INT(10) NOT NULL AUTO_INCREMENT,
  app_algorithm_id INT(10) NOT NULL,
  app_field_id     INT(10) NOT NULL,
  PRIMARY KEY (application_id),
  FOREIGN KEY (app_algorithm_id)
  REFERENCES algorithm (algorithm_id),
  FOREIGN KEY (app_field_id)
  REFERENCES field (field_id)
);
CREATE TABLE book (
  book_id        INT(10)     NOT NULL AUTO_INCREMENT,
  title          VARCHAR(50) NOT NULL,
  year_published SMALLINT    NOT NULL,
  edition        SMALLINT    NOT NULL,
  PRIMARY KEY (book_id)
);
CREATE TABLE author (
  author_id  INT(10)     NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(50) NOT NULL,
  last_name  VARCHAR(50) NOT NULL,
  PRIMARY KEY (author_id)
);
CREATE TABLE algorithm_reference (
  reference_id     INT(10) NOT NULL AUTO_INCREMENT,
  ref_algorithm_id INT(10) NOT NULL,
  ref_book_id      INT(10) NOT NULL,
  PRIMARY KEY (reference_id),
  FOREIGN KEY (ref_algorithm_id)
  REFERENCES algorithm (algorithm_id),
  FOREIGN KEY (ref_book_id)
  REFERENCES book (book_id)
);
CREATE TABLE textbook (
  textbook_id     INT(10) NOT NULL AUTO_INCREMENT,
  txtbk_book_id   INT(10) NOT NULL,
  txtbk_author_id INT(10) NOT NULL,
  PRIMARY KEY (textbook_id),
  FOREIGN KEY (txtbk_book_id)
  REFERENCES book (book_id),
  FOREIGN KEY (txtbk_author_id)
  REFERENCES author (author_id)
);


INSERT INTO book (title, edition) VALUES ('Introduction to algorithms', 3);
INSERT INTO book (title, volume, edition) VALUES ('The art of computer programming', 1, 3),
  ('The art of computer programming', 2, 3), ('The art of computer programming', 3, 2);
INSERT INTO author (first_name, last_name) VALUES ('Donald', 'Knuth'), ('Thomas', 'Cormen'),
  ('Charles', 'Leiserson'), ('Ronald', 'Rivest'), ('Clifford', 'Stein');
INSERT INTO textbook (txtbk_book_id, txtbk_author_id) VALUES (2, 1), (3, 1), (4, 1), (1, 2), (1, 3), (1, 4), (1, 5);
INSERT INTO design_paradigm (paradigm) VALUES ('Divide and conquer'), ('Dynamic programming'), ('Greedy strategy');
INSERT INTO field_of_study (name) VALUES ('graph theory'), ('searching'), ('sorting'),
  ('number theory'), ('linear programming'), ('matrix operations'), ('multithreaded algorithms'),
  ('computational geametry'), ('string algorithms'), ('approximation algorithms'), ('data structures');
INSERT INTO field_of_study (name) VALUES ('combinatorial optimization');
INSERT INTO area_of_use (name, description)
VALUES ('Electrical circuit design', 'Optiman interconnection of electrical pins with minimum amount of wire');
INSERT INTO algorithm (name, complexity, algo_field_id, algo_paradigm_id)
VALUES ('Kruscal algorithm', 'O(Eα(V))', 12, 3);
INSERT INTO algorithm (name, complexity, algo_field_id, algo_paradigm_id) VALUES ('Prim algorithm', 'O(Elg(V))', 12, 3);
INSERT INTO algorithm (name, complexity, algo_field_id, algo_paradigm_id)
VALUES ('Dijkstra algorithm', 'O(Elg(V))', 12, 3);
INSERT INTO algorithm_application (app_algorithm_id, app_area_id) VALUES (1, 1), (2, 1);
INSERT INTO algorithm (algorithm, complexity, algo_paradigm_id, algo_field_id)
  VALUE ('Dijkstra\'s algorithm', 'E*lgV', 3, 1);

SHOW TABLES;
SHOW CREATE TABLE algorithm;

ALTER TABLE algorithm_reference
  CHANGE COLUMN ref_book_id ref_textbook_id INT(10) NOT NULL;
ALTER TABLE algorithm_reference
  ADD COLUMN ref_textbook_id INT(10) NOT NULL;
ALTER TABLE algorithm_reference
  ADD CONSTRAINT `ref_textbook_id` FOREIGN KEY (ref_textbook_id) REFERENCES textbook (textbook_id);
ALTER TABLE book
  DROP COLUMN year_published;
ALTER TABLE book
  ADD COLUMN volume TINYINT DEFAULT NULL
  AFTER title;
ALTER TABLE field_of_use
RENAME area_of_use;
ALTER TABLE area_of_use
  CHANGE COLUMN field_id area_id INT(10) NOT NULL AUTO_INCREMENT;
ALTER TABLE algorithm_application
  CHANGE COLUMN app_field_id app_area_id INT(10) NOT NULL;
ALTER TABLE algorithm
  ADD COLUMN algo_field_id INT(10) NOT NULL
  AFTER complexity;
ALTER TABLE algorithm
  ADD CONSTRAINT algo_field_id FOREIGN KEY (algo_field_id) REFERENCES field_of_study (field_id);
ALTER TABLE design_paradigm
  ADD COLUMN description VARCHAR(255) DEFAULT NULL;
ALTER TABLE implementation_type
  ADD COLUMN description VARCHAR(255) DEFAULT NULL;
ALTER TABLE algorithm
  DROP COLUMN algo_implementation_id;
DROP TABLE implementation_type;
ALTER TABLE algorithm
  CHANGE algorithm_name algorithm VARCHAR(100) NOT NULL;
ALTER TABLE field_of_study
  CHANGE field_name field VARCHAR(100) NOT NULL;
ALTER TABLE area_of_use
  CHANGE name area VARCHAR(50) NOT NULL;
ALTER TABLE design_paradigm
  ADD UNIQUE (paradigm);
ALTER TABLE field_of_study
  ADD UNIQUE (field);
ALTER TABLE algorithm
  ADD UNIQUE (algorithm);
ALTER TABLE algorithm_reference
  CHANGE ref_textbook_id ref_book_id INT NOT NULL;
ALTER TABLE algorithm_reference
  ADD CONSTRAINT `fk_book_reference` FOREIGN KEY (ref_book_id) REFERENCES book (book_id);

DESCRIBE design_paradigm;
DESCRIBE algorithm;
DESCRIBE algorithm_reference;
DESCRIBE book;
SELECT *
FROM book;
SELECT *
FROM author;
SELECT *
FROM design_paradigm;
SELECT *
FROM field_of_study;
SELECT *
FROM algorithm;
SELECT *
FROM area_of_use;
SELECT *
FROM textbook;
SELECT
  title,
  volume,
  edition,
  first_name,
  last_name
FROM
  ((book
    INNER JOIN textbook ON book_id = txtbk_book_id)
    INNER JOIN author ON txtbk_author_id = author_id);
SELECT
  algorithm.name,
  area_of_use.name AS `area of use`,
  description      AS `area's description`
FROM
  ((algorithm
    INNER JOIN algorithm_application ON algorithm_id = app_algorithm_id)
    INNER JOIN area_of_use ON app_area_id = area_id);
SELECT LAST_INSERT_ID();

SELECT
  algorithm_id,
  algorithm,
  complexity,
  paradigm,
  algo_paradigm_id,
  field,
  algo_field_id
FROM
  ((algorithm
    INNER JOIN design_paradigm ON algo_paradigm_id = paradigm_id)
    INNER JOIN field_of_study ON algo_field_id = field_id)
WHERE 1 = 1 && paradigm = 'Dynamic programming';

SELECT
  book_id,
  title,
  volume,
  edition,
  author_id,
  first_name,
  last_name
FROM
  ((book
    INNER JOIN textbook ON book_id = txtbk_book_id)
    INNER JOIN author ON txtbk_author_id = author_id)
WHERE CONCAT(first_name, last_name) LIKE '%homas%' OR CONCAT(first_name, last_name) LIKE '%les%'
ORDER BY book_id, author_id;

SELECT DISTINCT book_id
FROM
  ((book
    INNER JOIN textbook ON book_id = txtbk_book_id)
    INNER JOIN author ON txtbk_author_id = author_id)
WHERE
  CONCAT(first_name, last_name) LIKE '%Thomas%';

SELECT DISTINCT book_id
FROM
  ((book
    INNER JOIN textbook ON book_id = txtbk_book_id)
    INNER JOIN author ON txtbk_author_id = author_id)
WHERE
  1 = 1
  AND (1 != 1
       OR CONCAT(first_name, last_name) LIKE '%Thomas%');

SELECT
  book_id,
  title,
  volume,
  edition,
  author_id,
  first_name,
  last_name
FROM
  ((book
    INNER JOIN textbook ON book_id = txtbk_book_id)
    INNER JOIN author ON txtbk_author_id = author_id)
ORDER BY book_id, author_id;

SELECT
  b2.book_id,
  b2.title,
  b2.volume,
  b2.edition,
  a2.author_id,
  a2.first_name,
  a2.last_name
FROM
  ((SELECT
      b1.book_id,
      b1.title,
      b1.edition,
      b1.volume
    FROM
      ((author AS a1
        INNER JOIN textbook AS t1 ON a1.author_id = t1.txtbk_author_id)
        INNER JOIN book AS b1 ON t1.txtbk_book_id = b1.book_id)
    WHERE
      CONCAT(a1.first_name, a1.first_name) LIKE '%Charles%') AS b2
    INNER JOIN textbook AS t2 ON b2.book_id = t2.txtbk_book_id)
  INNER JOIN author AS a2 ON a2.author_id = t2.txtbk_author_id
ORDER BY b2.book_id, a2.author_id;


SELECT *
FROM book AS b INNER JOIN textbook AS t ON b.book_id = t.txtbk_book_id;

SELECT
  algorithm_id,
  algorithm,
  complexity,
  paradigm_id,
  paradigm,
  dp.description,
  field_id,
  field,
  fos.description
FROM
  ((algorithm
    INNER JOIN design_paradigm AS dp ON algo_paradigm_id = paradigm_id)
    INNER JOIN field_of_study AS fos ON algo_field_id = field_id);
    
    SELECT 
    area_id, area, description
FROM
    (SELECT 
        app_area_id
    FROM
        algorithm_application
    WHERE
        app_algorithm_id = 1) as areas
        INNER JOIN
    area_of_use ON areas.app_area_id = area_id;
    
    SELECT
        COUNT(*) 
    FROM
        algorithm_application 
    WHERE
        app_algorithm_id = 27 
        AND app_area_id = 2;
        
SELECT 
    algorithm_id,
    algorithm,
    complexity,
    paradigm_id,
    paradigm,
    dp.description,
    field_id,
    field,
    f.description
FROM
    (SELECT 
        app_algorithm_id
    FROM
        algorithm_application
    WHERE
        app_area_id = 1) AS algos
        INNER JOIN
    algorithm ON algos.app_algorithm_id = algorithm_id
        INNER JOIN
    design_paradigm as dp ON algo_paradigm_id = paradigm_id
        INNER JOIN
    field_of_study as f ON algo_field_id = field_id
ORDER BY algorithm_id;

select book_id, title, volume, edition, author_id, first_name, last_name
from (select ref_book_id from algorithm_reference where ref_algorithm_id = 1) as algos
inner join book on algos.ref_book_id = book_id inner join textbook on book_id = txtbk_book_id 
inner join author on txtbk_author_id = author_id
order by book_id, author_id;

SELECT author_id from author WHERE author_id = 1 LIMIT 1;








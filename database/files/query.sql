USE algorithms;
CREATE TABLE design_paradigm (
    paradigm_id INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    paradigm VARCHAR(50) NOT NULL
);
CREATE TABLE field_of_study (
    field_id INT(10) NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (field_id)
);
CREATE TABLE implementation_type (
    implementation_id INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(100) NOT NULL
);
CREATE TABLE algorithm (
    algorithm_id INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    complexity VARCHAR(60) NOT NULL,
    algo_paradigm_id INT(10) NOT NULL,
    algo_implementation_id INT(10) NOT NULL,
    FOREIGN KEY (algo_paradigm_id)
        REFERENCES design_paradigm (paradigm_id),
    FOREIGN KEY (algo_implementation_id)
        REFERENCES implementation_type (implementation_id)
);
CREATE TABLE field (
    field_id INT(10) NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (field_id)
);
CREATE TABLE algorithm_application (
    application_id INT(10) NOT NULL AUTO_INCREMENT,
    app_algorithm_id INT(10) NOT NULL,
    app_field_id INT(10) NOT NULL,
    PRIMARY KEY (application_id),
    FOREIGN KEY (app_algorithm_id)
        REFERENCES algorithm (algorithm_id),
    FOREIGN KEY (app_field_id)
        REFERENCES field (field_id)
);
CREATE TABLE book (
    book_id INT(10) NOT NULL AUTO_INCREMENT,
    title VARCHAR(50) NOT NULL,
    year_published SMALLINT NOT NULL,
    edition SMALLINT NOT NULL,
    PRIMARY KEY (book_id)
);
CREATE TABLE author (
    author_id INT(10) NOT NULL AUTO_INCREMENT,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    PRIMARY KEY (author_id)
);
CREATE TABLE algorithm_reference (
    reference_id INT(10) NOT NULL AUTO_INCREMENT,
    ref_algorithm_id INT(10) NOT NULL,
    ref_book_id INT(10) NOT NULL,
    PRIMARY KEY (reference_id),
    FOREIGN KEY (ref_algorithm_id)
        REFERENCES algorithm (algorithm_id),
    FOREIGN KEY (ref_book_id)
        REFERENCES book (book_id)
);
CREATE TABLE textbook (
    textbook_id INT(10) NOT NULL AUTO_INCREMENT,
    txtbk_book_id INT(10) NOT NULL,
    txtbk_author_id INT(10) NOT NULL,
    PRIMARY KEY (textbook_id),
    FOREIGN KEY (txtbk_book_id)
        REFERENCES book (book_id),
    FOREIGN KEY (txtbk_author_id)
        REFERENCES author (author_id)
);


INSERT INTO book (title, edition)  VALUES ('Introduction to algorithms', 3);
INSERT INTO book (title, volume, edition) VALUES('The art of computer programming', 1, 3), 
('The art of computer programming', 2, 3),  ('The art of computer programming', 3, 2);
INSERT INTO author (firstName, lastName) VALUES ('Donald', 'Knuth'), ('Thomas', 'Cormen'),
('Charles', 'Leiserson'), ('Ronald', 'Rivest'), ('Clifford', 'Stein');     
INSERT INTO textbook (txtbk_book_id, txtbk_author_id) VALUES (2, 1), (3, 1), (4, 1), (1, 2), (1, 3), (1, 4), (1, 5);  
INSERT INTO design_paradigm (paradigm) VALUES ('Divide and conquer'), ('Dynamic programming'), ('Greedy strategy');  
INSERT INTO field_of_study (name) VALUES ('graph theory'), ('searching'), ('sorting'), 
('number theory'), ('linear programming'), ('matrix operations'), ('multithreaded algorithms'), 
('computational geametry'), ('string algorithms'), ('approximation algorithms'), ('data structures');
insert into field_of_study (name) values ('combinatorial optimization');
insert into area_of_use (name, description) values ('Electrical circuit design', 'Optiman interconnection of electrical pins with minimum amount of wire');
insert into algorithm (name, complexity, algo_field_id, algo_paradigm_id) values ('Kruscal algorithm', 'O(Eα(V))', 12, 3); 
insert into algorithm (name, complexity, algo_field_id, algo_paradigm_id) values ('Prim algorithm', 'O(Elg(V))', 12, 3); 
insert into algorithm (name, complexity, algo_field_id, algo_paradigm_id) values ('Dijkstra algorithm', 'O(Elg(V))', 12, 3); 
insert into algorithm_application (app_algorithm_id, app_area_id) values (1, 1), (2, 1);  
  
  
SHOW TABLES;
SHOW CREATE TABLE algorithm;
    
ALTER TABLE algorithm_reference CHANGE COLUMN ref_book_id ref_textbook_id INT(10) NOT NULL;
ALTER TABLE algorithm_reference ADD COLUMN ref_textbook_id INT(10) NOT NULL;
ALTER TABLE algorithm_reference ADD CONSTRAINT `ref_textbook_id` FOREIGN KEY(ref_textbook_id) REFERENCES textbook(textbook_id);
ALTER TABLE book DROP COLUMN year_published;
ALTER TABLE book ADD COLUMN volume TINYINT DEFAULT NULL AFTER title;
ALTER TABLE field_of_use RENAME area_of_use;
ALTER TABLE area_of_use CHANGE COLUMN field_id area_id INT(10) NOT NULL AUTO_INCREMENT;
ALTER TABLE algorithm_application CHANGE COLUMN app_field_id app_area_id INT(10) NOT NULL;
ALTER TABLE algorithm ADD COLUMN algo_field_id INT(10) NOT NULL AFTER complexity;
ALTER TABLE algorithm ADD CONSTRAINT algo_field_id FOREIGN KEY(algo_field_id) REFERENCES field_of_study(field_id);
ALTER TABLE design_paradigm ADD COLUMN description VARCHAR(255) DEFAULT NULL;
ALTER TABLE implementation_type ADD COLUMN description VARCHAR(255) DEFAULT NULL;
alter table algorithm drop column algo_implementation_id;
drop table implementation_type;

DESCRIBE design_paradigm;
DESCRIBE algorithm;
DESCRIBE algorithm_reference;
DESCRIBE book;
SELECT * FROM book;
SELECT * FROM author;
SELECT * FROM design_paradigm;
select * from field_of_study;
select * from algorithm;
select * from area_of_use;
SELECT 
    title, volume, edition, firstName, lastName
FROM
    ((book
    INNER JOIN textbook ON book_id = txtbk_book_id)
    INNER JOIN author ON txtbk_author_id = author_id);
SELECT 
    algorithm.name,
    area_of_use.name AS `area of use`,
    description AS `area's description`
FROM
    ((algorithm
    INNER JOIN algorithm_application ON algorithm_id = app_algorithm_id)
    INNER JOIN area_of_use ON app_area_id = area_id);






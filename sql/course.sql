create table COURSE (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    TITLE VARCHAR(255) NOT NULL,
    TEACHER VARCHAR(255) NOT NULL,
    DTF VARCHAR(32) NOT NULL
)

CREATE TABLE STUDENT (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    FIRST_NAME VARCHAR(255) NOT NULL,
    LAST_NAME VARCHAR(255) NOT NULL,
    AGE TINYINT NOT NULL,
    PRACTISE_COMPANY VARCHAR(255) NOT NULL
)

CREATE TABLE LECTION (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    COURSE_ID INT NOT NULL,
    STUDENT_ID INT NOT NULL,
    LECTION_DTM DATETIME NOT NULL,
    IS_PRESENT BOOLEAN NOT NULL,
    MARK TINYINT,
    FOREIGN KEY (COURSE_ID) REFERENCES COURSE(ID),
    FOREIGN KEY (STUDENT_ID) REFERENCES STUDENT(ID)
)
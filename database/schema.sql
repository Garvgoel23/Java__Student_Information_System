-- PostgreSQL Schema for Student Information System (SIS)

CREATE TABLE IF NOT EXISTS admins (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS students (
    id SERIAL PRIMARY KEY,
    roll_no VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    branch VARCHAR(50),
    semester INT
);

CREATE TABLE IF NOT EXISTS marks (
    id SERIAL PRIMARY KEY,
    student_id INT,
    subject VARCHAR(100),
    marks_obtained INT,
    max_marks INT,
    CONSTRAINT fk_marks_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS attendance (
    id SERIAL PRIMARY KEY,
    student_id INT,
    subject VARCHAR(100),
    total_classes INT,
    attended INT,
    CONSTRAINT fk_attendance_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
);

-- Sample data
INSERT INTO admins (username, password) VALUES ('admin', 'admin123')
    ON CONFLICT (username) DO NOTHING;

INSERT INTO students (roll_no, name, email, password, branch, semester)
VALUES ('CS001', 'Rahul Sharma', 'rahul@college.com', 'pass123', 'CSE', 3)
    ON CONFLICT (roll_no) DO NOTHING;

INSERT INTO marks (student_id, subject, marks_obtained, max_marks) VALUES
(1, 'Advanced Java', 85, 100),
(1, 'DBMS', 78, 100),
(1, 'Operating Systems', 72, 100);

INSERT INTO attendance (student_id, subject, total_classes, attended) VALUES
(1, 'Advanced Java', 40, 36),
(1, 'DBMS', 38, 30),
(1, 'Operating Systems', 42, 38);

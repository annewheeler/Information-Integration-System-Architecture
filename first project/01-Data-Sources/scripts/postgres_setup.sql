-- 1. Crearea tabelului pentru Departamente
CREATE TABLE departments (
    dept_id SERIAL PRIMARY KEY,
    dept_name VARCHAR(100) UNIQUE NOT NULL
);

-- 2. Crearea tabelului pentru Domenii de Educație
CREATE TABLE education_fields (
    edu_id SERIAL PRIMARY KEY,
    field_name VARCHAR(100) UNIQUE NOT NULL
);

-- 3. Tabelul principal de angajați (Normalizat)
CREATE TABLE employees (
    employee_num INT PRIMARY KEY,
    age INT,
    gender VARCHAR(20),
    marital_status VARCHAR(20),
    dept_id INT,
    edu_id INT,
    CONSTRAINT fk_dept FOREIGN KEY(dept_id) REFERENCES departments(dept_id),
    CONSTRAINT fk_edu FOREIGN KEY(edu_id) REFERENCES education_fields(edu_id)
);

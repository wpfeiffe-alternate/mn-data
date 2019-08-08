DROP TABLE IF EXISTS employee;
DROP TABLE IF EXISTS department;
DROP TABLE IF EXISTS company;

DROP SEQUENCE IF EXISTS employee_id_seq;
DROP SEQUENCE IF EXISTS department_id_seq;
DROP SEQUENCE IF EXISTS company_id_seq;

CREATE SEQUENCE employee_id_seq START WITH 100000;
CREATE SEQUENCE department_id_seq START WITH 100000;
CREATE SEQUENCE company_id_seq START WITH 100000;

CREATE TABLE company
(
    id BIGINT DEFAULT (NEXT VALUE FOR company_id_seq),
    company_name VARCHAR(100) NOT NULL,

    CONSTRAINT company_pk PRIMARY KEY (id),
    CONSTRAINT company_name_index UNIQUE (company_name)
);

CREATE TABLE department
(
    id BIGINT DEFAULT (NEXT VALUE FOR department_id_seq),
    dept_code VARCHAR(20) NOT NULL,
    dept_name VARCHAR(30) NOT NULL,
    company_id BIGINT NOT NULL,

    CONSTRAINT dept_co_fk FOREIGN KEY (company_id) REFERENCES company(id),
    CONSTRAINT department_pk PRIMARY KEY (id),
    CONSTRAINT dept_code_co_index UNIQUE (dept_code, company_id),
    CONSTRAINT dept_name_co_index UNIQUE (dept_name, company_id)
);

CREATE TABLE employee
(
    id BIGINT DEFAULT (NEXT VALUE FOR employee_id_seq),
    first_name VARCHAR(40) NOT NULL,
    last_name VARCHAR(40) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT FALSE,
    start_date DATE NOT NULL,
    title VARCHAR(40) NOT NULL,
    dept_id BIGINT NOT NULL,

    CONSTRAINT emp_dept_fk FOREIGN KEY (dept_id) REFERENCES department(id),
    CONSTRAINT employee_pk PRIMARY KEY (id)
);

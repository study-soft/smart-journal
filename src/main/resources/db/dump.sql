------------------------- TABLES -------------------------

-- group
DROP TABLE IF EXISTS party CASCADE;
DROP SEQUENCE IF EXISTS party_id_seq CASCADE;
CREATE TABLE party (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    description VARCHAR(65535),
    created_by VARCHAR(50) NOT NULL,
    created TIMESTAMP,
    updated_by VARCHAR(50),
    updated TIMESTAMP
);

-- subject
DROP TABLE IF EXISTS subject CASCADE;
DROP SEQUENCE IF EXISTS subject_id_seq CASCADE;
CREATE TABLE subject (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    description VARCHAR(65535),
    created_by VARCHAR(50) NOT NULL,
    created TIMESTAMP,
    updated_by VARCHAR(50),
    updated TIMESTAMP
);

-- party_subject
DROP TABLE IF EXISTS party_subject CASCADE;
CREATE TABLE party_subject (
    subjects_id BIGINT NOT NULL,
    parties_id BIGINT NOT NULL
);

-- board
DROP TABLE IF EXISTS board CASCADE;
DROP SEQUENCE IF EXISTS board_id_seq CASCADE;
CREATE TABLE board (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    description VARCHAR(65535),
    created_by VARCHAR(50) NOT NULL,
    created TIMESTAMP,
    updated_by VARCHAR(50),
    updated TIMESTAMP,
    party_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL
);

-- day_type
DROP TABLE IF EXISTS day_type CASCADE;
DROP SEQUENCE IF EXISTS day_type_id_seq CASCADE;
CREATE TABLE day_type (
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    score DOUBLE PRECISION NOT NULL,
    description VARCHAR(65535),
    expiry INT,
    created_by VARCHAR(50) NOT NULL,
    created TIMESTAMP,
    updated_by VARCHAR(50),
    updated TIMESTAMP,
    board_id BIGINT NOT NULL
);

-- student
DROP TABLE IF EXISTS student CASCADE;
DROP SEQUENCE IF EXISTS student_id_seq CASCADE;
CREATE TABLE student (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    middle_name VARCHAR(255),
    rating DOUBLE PRECISION,
    board_id BIGINT NOT NULL
);

-- day
DROP TABLE IF EXISTS day CASCADE;
DROP SEQUENCE IF EXISTS day_id_seq CASCADE;
CREATE TABLE day (
    id BIGSERIAL PRIMARY KEY,
    date DATE NOT NULL,
    result DOUBLE PRECISION,
    student_id BIGINT NOT NULL,
    day_type_id BIGINT NOT NULL
);
-- authority
DROP TABLE IF EXISTS authority CASCADE;
CREATE TABLE authority (
    name VARCHAR(50) PRIMARY KEY
);

-- app_user
DROP TABLE IF EXISTS app_user CASCADE;
DROP SEQUENCE IF EXISTS app_user_id_seq CASCADE;
CREATE TABLE app_user (
    id BIGSERIAL PRIMARY KEY,
    login VARCHAR(50) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(254),
    image_url VARCHAR(256),
    activated BOOLEAN NOT NULL,
    lang_key VARCHAR(6),
    activation_key VARCHAR(20),
    reset_key VARCHAR(20),
    created_by VARCHAR(50) NOT NULL,
    created TIMESTAMP,
    reset_date TIMESTAMP,
    updated_by VARCHAR(50),
    updated TIMESTAMP
);

-- app_user_authority
DROP TABLE IF EXISTS app_user_authority CASCADE;
CREATE TABLE app_user_authority (
    user_id BIGINT NOT NULL,
    authority_name VARCHAR(50) NOT NULL
);

------------------------- INDICES -------------------------

CREATE UNIQUE INDEX pk_party ON party (id ASC);
CREATE UNIQUE INDEX pk_subject ON subject (id ASC);
CREATE UNIQUE INDEX pk_party_subject ON party_subject (subjects_id ASC, parties_id ASC);
CREATE UNIQUE INDEX pk_board ON board (id ASC);
CREATE INDEX ux_board_party_id ON board (party_id ASC);
CREATE INDEX ux_board_subject_id ON board (subject_id ASC);
CREATE UNIQUE INDEX pk_day_type ON day_type (id ASC);
CREATE UNIQUE INDEX pk_student ON student (id ASC);
CREATE UNIQUE INDEX pk_day ON day (id ASC);
CREATE INDEX ux_day_day_type_id ON day (day_type_id ASC);

CREATE UNIQUE INDEX pk_authority ON authority (name ASC);
CREATE UNIQUE INDEX pk_app_user ON app_user (id ASC);
CREATE UNIQUE INDEX ux_app_user_email ON app_user (email ASC);
CREATE UNIQUE INDEX ux_app_user_login ON app_user (login ASC);
CREATE UNIQUE INDEX pk_app_user_authority ON app_user_authority (user_id ASC, authority_name ASC);

------------------------- CONSTRAINTS -------------------------

-- board
ALTER TABLE board ADD CONSTRAINT fk_board_party_id FOREIGN KEY (party_id) REFERENCES party (id);
ALTER TABLE board ADD CONSTRAINT fk_board_subject_id FOREIGN KEY (subject_id) REFERENCES subject (id);

-- day_type
ALTER TABLE day_type ADD CONSTRAINT fk_day_type_board_id FOREIGN KEY (board_id) REFERENCES board (id);

-- student
ALTER TABLE student ADD CONSTRAINT fk_student_board_id FOREIGN KEY (board_id) REFERENCES board (id);

-- day
ALTER TABLE day ADD CONSTRAINT fk_day_day_type_id FOREIGN KEY (day_type_id) REFERENCES day_type (id);
ALTER TABLE day ADD CONSTRAINT fk_day_student_id FOREIGN KEY (student_id) REFERENCES student (id);

-- app_user_authority
ALTER TABLE app_user_authority ADD CONSTRAINT pkey_app_user_authority PRIMARY KEY (user_id, authority_name);
ALTER TABLE app_user_authority ADD CONSTRAINT fk_authority_name FOREIGN KEY (authority_name) REFERENCES authority (name);
ALTER TABLE app_user_authority ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES app_user (id);

------------------------- INSERT DATA -------------------------
-- group
INSERT INTO party (name, description, created_by, created, updated_by, updated) VALUES
    ('Інформатики', 'Тестова група для перевірки роботи розумного журналу. Пишу багато символів в описі, щоб перевірити, як це буде виглядати на сторінці. Ліміт сиволів - 65535, тому в мене ще є великий запас. Бла-бла-бла...', 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP);

-- subject
INSERT INTO subject (name, description, created_by, created, updated_by, updated) VALUES
    ('Комп’ютерні мережі', NULL, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP);

-- party_subject
INSERT INTO party_subject (subjects_id, parties_id) VALUES
    (1, 1);

-- board
INSERT INTO board (name, description, created_by, created, updated_by, updated, party_id, subject_id) VALUES
    ('Інформатики. Комп’ютерні мережі', '', 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 1, 1);

-- day_type
INSERT INTO day_type (type, score, description, expiry, created_by, created, updated_by, updated, board_id) VALUES
    ('SIMPLE', 1.0, 'Звичайна пара', NULL, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 1),
    ('LAB', 5.0, 'Лабараторна робота', 3, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 1),
    ('MODULE', 10.0, 'Модуль', NULL, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 1),
    ('EXAM', 20.0, 'Екзамен', NULL, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 1),
    ('TEST', 3.0, NULL, NULL, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 1);

-- student
INSERT INTO student (first_name, last_name, middle_name, rating, board_id) VALUES
    ('Антон', 'Яковенко', 'Сергійович', NULL, 1),
    ('Артем', 'Яковенко', 'Сергійович', NULL, 1),
    ('Джейсон', 'Стетхем', NULL, NULL, 1),
    ('Довге-довге ім’я', 'Довге-довге прізвище', 'Довге-довге ім’я по-батькові', 44.0, 1);

-- day
INSERT INTO day (date, result, student_id, day_type_id) VALUES
    (CURRENT_DATE - integer '28', 0.0, 1, 1),
    (CURRENT_DATE - integer '28', 1.0, 2, 1),
    (CURRENT_DATE - integer '28', 0.0, 3, 1),
    (CURRENT_DATE - integer '28', 1.0, 4, 1),
    (CURRENT_DATE - integer '21', 1.0, 1, 2),
    (CURRENT_DATE - integer '21', 2.0, 2, 2),
    (CURRENT_DATE - integer '21', 3.0, 3, 2),
    (CURRENT_DATE - integer '21', 4.0, 4, 2),
    (CURRENT_DATE - integer '14', 0.0, 1, 3),
    (CURRENT_DATE - integer '14', 2.0, 2, 3),
    (CURRENT_DATE - integer '14', 4.0, 3, 3),
    (CURRENT_DATE - integer '14', 6.0, 4, 3),
    (CURRENT_DATE - integer '7', 4.0, 1, 4),
    (CURRENT_DATE - integer '7', 8.0, 2, 4),
    (CURRENT_DATE - integer '7', 12.0, 3, 4),
    (CURRENT_DATE - integer '7', 16.0, 4, 4),
    (CURRENT_DATE, 0.0, 1, 5),
    (CURRENT_DATE, 1.0, 2, 5),
    (CURRENT_DATE, 2.0, 3, 5),
    (CURRENT_DATE, 3.0, 4, 5),
    (CURRENT_DATE + integer '7', NULL, 1, 1),
    (CURRENT_DATE + integer '7', NULL, 2, 1),
    (CURRENT_DATE + integer '7', NULL, 3, 1),
    (CURRENT_DATE + integer '7', NULL, 4, 1);

-- authority
INSERT INTO authority (name) VALUES
    ('ROLE_ADMIN'), ('ROLE_USER');

-- app_user
INSERT INTO app_user (login, password_hash, first_name, last_name, email, image_url, activated, lang_key, activation_key, reset_key, created_by, created, reset_date, updated_by, updated) VALUES
    ('system', '$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG', 'System', 'System', 'system@localhost', '', TRUE, 'ua', NULL, NULL, 'system', NULL, NULL, 'system', NULL),
    ('anonymoususer', '$2a$10$j8S5d7Sr7.8VTOYNviDPOeWX8KcYILUVJBsYV83Y5NtECayypx9lO', 'Anonymous', 'System', 'anonymous@localhost', '', TRUE, 'ua', NULL, NULL, 'system', NULL, NULL, 'system', NULL),
    ('admin', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', 'System', 'Administrator', 'admin@localhost', '', TRUE, 'ua', NULL, NULL, 'system', NULL, NULL, 'system', NULL),
    ('user', '$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K', 'System', 'User', 'user@localhost', '', TRUE, 'ua', NULL, NULL, 'system', NULL, NULL, 'system', NULL);

-- app_user_authority
INSERT INTO app_user_authority (user_id, authority_name) VALUES
    (1, 'ROLE_ADMIN'), (1, 'ROLE_USER'), (3, 'ROLE_ADMIN'), (3, 'ROLE_USER'), (4, 'ROLE_USER');

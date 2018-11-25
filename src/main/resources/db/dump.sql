------------------------- TABLES -------------------------

-- party
DROP TABLE IF EXISTS party CASCADE;
DROP SEQUENCE IF EXISTS party_id_seq CASCADE;
CREATE TABLE party (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    description VARCHAR(65535),
    created TIMESTAMP NOT NULL,
    updated TIMESTAMP NOT NULL
);

-- subject
DROP TABLE IF EXISTS subject CASCADE;
DROP SEQUENCE IF EXISTS subject_id_seq CASCADE;
CREATE TABLE subject (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    description VARCHAR(65535),
    created TIMESTAMP NOT NULL,
    updated TIMESTAMP NOT NULL
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
    created TIMESTAMP NOT NULL,
    updated TIMESTAMP NOT NULL,
    party_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL
);

-- day_type
DROP TABLE IF EXISTS day_type CASCADE;
DROP SEQUENCE IF EXISTS day_type_id_seq CASCADE;
CREATE TABLE day_type (
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    score DOUBLE PRECISION NOT NULL CHECK(score >= 0),
    description VARCHAR(65535) NULL,
    created TIMESTAMP NOT NULL,
    updated TIMESTAMP NOT NULL,
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
    rating DOUBLE PRECISION CHECK(rating >= 0),
    created TIMESTAMP NOT NULL,
    updated TIMESTAMP NOT NULL,
    board_id BIGINT NOT NULL
);

-- day
DROP TABLE IF EXISTS day CASCADE;
DROP SEQUENCE IF EXISTS day_id_seq CASCADE;
CREATE TABLE day (
    id BIGSERIAL PRIMARY KEY,
    date TIMESTAMP NOT NULL,
    result DOUBLE PRECISION CHECK(result >= 0),
    student_id BIGINT NOT NULL,
    day_type_id BIGINT NOT NULL
);
-- jhi_authority
DROP TABLE IF EXISTS jhi_authority CASCADE;
CREATE TABLE jhi_authority (
    name VARCHAR(50) PRIMARY KEY
);

-- jhi_persistent_audit_event
DROP TABLE IF EXISTS jhi_persistent_audit_event CASCADE;
DROP SEQUENCE IF EXISTS jhi_persistent_audit_event_event_id_seq CASCADE;
CREATE TABLE jhi_persistent_audit_event (
    event_id BIGSERIAL PRIMARY KEY,
    principal VARCHAR(50) NOT NULL,
    event_date TIMESTAMP,
    event_type VARCHAR(255)
);

-- jhi_persistent_audit_evt_data
DROP TABLE IF EXISTS jhi_persistent_audit_evt_data CASCADE;
CREATE TABLE jhi_persistent_audit_evt_data (
    event_id BIGINT NOT NULL,
    name VARCHAR(150) NOT NULL,
    value VARCHAR(255)
);

-- jhi_user
DROP TABLE IF EXISTS jhi_user CASCADE;
DROP SEQUENCE IF EXISTS jhi_user_id_seq CASCADE;
CREATE TABLE jhi_user (
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
    created_date TIMESTAMP,
    reset_date TIMESTAMP,
    last_modified_by VARCHAR(50),
    last_modified_date TIMESTAMP
);

-- jhi_user_authority
DROP TABLE IF EXISTS jhi_user_authority CASCADE;
CREATE TABLE jhi_user_authority (
    user_id BIGINT NOT NULL,
    authority_name VARCHAR(50) NOT NULL
);

DROP SEQUENCE IF EXISTS hibernate_sequence CASCADE;
CREATE SEQUENCE hibernate_sequence;

------------------------- INDICES -------------------------

CREATE UNIQUE INDEX pk_party ON party (id ASC);
CREATE UNIQUE INDEX pk_subject ON subject (id ASC);
CREATE UNIQUE INDEX pk_party_subject ON party_subject (subjects_id ASC, parties_id ASC);
CREATE UNIQUE INDEX pk_board ON board (id ASC);
CREATE UNIQUE INDEX ux_board_party_id ON board (party_id ASC);
CREATE UNIQUE INDEX ux_board_subject_id ON board (subject_id ASC);
CREATE UNIQUE INDEX pk_day_type ON day_type (id ASC);
CREATE UNIQUE INDEX pk_student ON student (id ASC);
CREATE UNIQUE INDEX pk_day ON day (id ASC);
CREATE INDEX ux_day_day_type_id ON day (day_type_id ASC);

CREATE UNIQUE INDEX pk_jhi_authority ON jhi_authority (name ASC);
CREATE INDEX idx_persistent_audit_event ON jhi_persistent_audit_event (principal ASC, event_date ASC);
CREATE UNIQUE INDEX pk_jhi_persistent_audit_event ON jhi_persistent_audit_event (event_id ASC);
CREATE INDEX idx_persistent_audit_evt_data ON jhi_persistent_audit_evt_data (event_id ASC);
CREATE UNIQUE INDEX pk_jhi_persistent_audit_evt_data ON jhi_persistent_audit_evt_data (event_id ASC, name ASC);
CREATE UNIQUE INDEX pk_jhi_user ON jhi_user (id ASC);
CREATE UNIQUE INDEX ux_user_email ON jhi_user (email ASC);
CREATE UNIQUE INDEX ux_user_login ON jhi_user (login ASC);
CREATE UNIQUE INDEX pk_jhi_user_authority ON jhi_user_authority (user_id ASC, authority_name ASC);

------------------------- KEYS -------------------------

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

-- jhi_persistent_audit_evt_data
ALTER TABLE jhi_persistent_audit_evt_data ADD CONSTRAINT pkey_jhi_persistent_audit_evt_data PRIMARY KEY (event_id, name);
ALTER TABLE jhi_persistent_audit_evt_data ADD CONSTRAINT fk_evt_pers_audit_evt_data FOREIGN KEY (event_id)
REFERENCES jhi_persistent_audit_event (event_id);

-- jhi_user_authority
ALTER TABLE jhi_user_authority ADD CONSTRAINT pkey_jhi_user_authority PRIMARY KEY (user_id, authority_name);
ALTER TABLE jhi_user_authority ADD CONSTRAINT fk_authority_name FOREIGN KEY (authority_name) REFERENCES jhi_authority (name);
ALTER TABLE jhi_user_authority ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES jhi_user (id);

------------------------- INSERT DATA -------------------------
-- party
INSERT INTO party (name, description, created, updated) VALUES
    ('Інформатики', 'Тестова група для перевірки роботи розумного журналу. Пишу багато символів в описі, щоб перевірити, як це буде виглядати на сторінці. Ліміт сиволів - 65535, тому в мене ще є великий запас. Бла-бла-бла...', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- subject
INSERT INTO subject (name, description, created, updated) VALUES
    ('Комп’ютерні мережі', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- party_subject
INSERT INTO party_subject (subjects_id, parties_id) VALUES
    (1, 1);

-- board
INSERT INTO board (name, description, created, updated, party_id, subject_id) VALUES
    ('Інформатики. Комп’ютерні мережі', '', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1);

-- day_type
INSERT INTO day_type (type, score, description, created, updated, board_id) VALUES
    ('SIMPLE', 1.0, 'Звичайна пара', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
    ('LAB', 5.0, 'Лабараторна робота', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
    ('MODULE', 10.0, 'Модуль', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
    ('EXAM', 20.0, 'Екзамен', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
    ('TEST', 3.0, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1);

-- student
INSERT INTO student (first_name, last_name, middle_name, rating, created, updated, board_id) VALUES
    ('Антон', 'Яковенко', 'Сергійович', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
    ('Артем', 'Яковенко', 'Сергійович', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
    ('Джейсон', 'Стетхем', NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
    ('Довге-довге-довге ім’я', 'Довге-довге-довге прізвище', 'Довге-довге-довге ім’я по-батькові', 44, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1);

-- day
INSERT INTO day (date, result, student_id, day_type_id) VALUES
    (CURRENT_TIMESTAMP, 0.0, 1, 1),
    (CURRENT_TIMESTAMP, 1.0, 2, 1),
    (CURRENT_TIMESTAMP, 0.0, 3, 1),
    (CURRENT_TIMESTAMP, 1.0, 4, 1),
    (CURRENT_TIMESTAMP, 1.0, 1, 2),
    (CURRENT_TIMESTAMP, 2.0, 2, 2),
    (CURRENT_TIMESTAMP, 3.0, 3, 2),
    (CURRENT_TIMESTAMP, 4.0, 4, 2),
    (CURRENT_TIMESTAMP, 0.0, 1, 3),
    (CURRENT_TIMESTAMP, 2.0, 2, 3),
    (CURRENT_TIMESTAMP, 4.0, 3, 3),
    (CURRENT_TIMESTAMP, 6.0, 4, 3),
    (CURRENT_TIMESTAMP, 4.0, 1, 4),
    (CURRENT_TIMESTAMP, 8.0, 2, 4),
    (CURRENT_TIMESTAMP, 12.0, 3, 4),
    (CURRENT_TIMESTAMP, 16.0, 4, 4),
    (CURRENT_TIMESTAMP, 0.0, 1, 5),
    (CURRENT_TIMESTAMP, 1.0, 2, 5),
    (CURRENT_TIMESTAMP, 2.0, 3, 5),
    (CURRENT_TIMESTAMP, 3.0, 4, 5);

-- jhi_authority
INSERT INTO jhi_authority (name) VALUES
    ('ROLE_ADMIN'), ('ROLE_USER');

-- jhi_user
INSERT INTO jhi_user (login, password_hash, first_name, last_name, email, image_url, activated, lang_key, activation_key, reset_key, created_by, created_date, reset_date, last_modified_by, last_modified_date) VALUES
    ('system', '$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG', 'System', 'System', 'system@localhost', '', TRUE, 'ua', NULL, NULL, 'system', NULL, NULL, 'system', NULL),
    ('anonymoususer', '$2a$10$j8S5d7Sr7.8VTOYNviDPOeWX8KcYILUVJBsYV83Y5NtECayypx9lO', 'Anonymous', 'System', 'anonymous@localhost', '', TRUE, 'ua', NULL, NULL, 'system', NULL, NULL, 'system', NULL),
    ('admin', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', 'System', 'Administrator', 'admin@localhost', '', TRUE, 'ua', NULL, NULL, 'system', NULL, NULL, 'system', NULL),
    ('user', '$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K', 'System', 'User', 'user@localhost', '', TRUE, 'ua', NULL, NULL, 'system', NULL, NULL, 'system', NULL);

-- jhi_user_authority
INSERT INTO jhi_user_authority (user_id, authority_name) VALUES
    (1, 'ROLE_ADMIN'), (1, 'ROLE_USER'), (3, 'ROLE_ADMIN'), (3, 'ROLE_USER'), (4, 'ROLE_USER');


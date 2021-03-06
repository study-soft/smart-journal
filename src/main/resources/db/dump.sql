------------------------- TABLES -------------------------

-- _groups
DROP TABLE IF EXISTS _groups CASCADE;
DROP SEQUENCE IF EXISTS _groups_id_seq CASCADE;
CREATE TABLE _groups (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(65535),
    created_by VARCHAR(50) NOT NULL,
    created TIMESTAMP,
    updated_by VARCHAR(50),
    updated TIMESTAMP,
    user_id BIGINT NOT NULL
);

-- subjects
DROP TABLE IF EXISTS subjects CASCADE;
DROP SEQUENCE IF EXISTS subjects_id_seq CASCADE;
CREATE TABLE subjects (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(65535),
    created_by VARCHAR(50) NOT NULL,
    created TIMESTAMP,
    updated_by VARCHAR(50),
    updated TIMESTAMP,
    user_id BIGINT NOT NULL
);

-- groups_subjects
DROP TABLE IF EXISTS groups_subjects CASCADE;
CREATE TABLE groups_subjects (
    groups_id BIGINT NOT NULL,
    subjects_id BIGINT NOT NULL
);

-- boards
DROP TABLE IF EXISTS boards CASCADE;
DROP SEQUENCE IF EXISTS boards_id_seq CASCADE;
CREATE TABLE boards (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    description VARCHAR(65535),
    total_score DOUBLE PRECISION,
    created_by VARCHAR(50) NOT NULL,
    created TIMESTAMP,
    updated_by VARCHAR(50),
    updated TIMESTAMP,
    group_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL
);

-- day_types
DROP TABLE IF EXISTS day_types CASCADE;
DROP SEQUENCE IF EXISTS day_types_id_seq CASCADE;
CREATE TABLE day_types (
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    score DOUBLE PRECISION NOT NULL,
    name VARCHAR(255),
    description VARCHAR(65535),
    expiry INT,
    created_by VARCHAR(50) NOT NULL,
    created TIMESTAMP,
    updated_by VARCHAR(50),
    updated TIMESTAMP,
    board_id BIGINT NOT NULL
);

-- students
DROP TABLE IF EXISTS students CASCADE;
DROP SEQUENCE IF EXISTS students_id_seq CASCADE;
CREATE TABLE students (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    middle_name VARCHAR(255),
    group_id BIGINT NOT NULL
);

-- days
DROP TABLE IF EXISTS days CASCADE;
DROP SEQUENCE IF EXISTS days_id_seq CASCADE;
CREATE TABLE days (
    id BIGSERIAL PRIMARY KEY,
    date DATE NOT NULL,
    result DOUBLE PRECISION,
    student_id BIGINT NOT NULL,
    day_type_id BIGINT NOT NULL
);

-- authorities
DROP TABLE IF EXISTS authorities CASCADE;
CREATE TABLE authorities (
    name VARCHAR(50) PRIMARY KEY
);

-- users
DROP TABLE IF EXISTS users CASCADE;
DROP SEQUENCE IF EXISTS users_id_seq CASCADE;
CREATE TABLE users (
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

-- users_authorities
DROP TABLE IF EXISTS users_authorities CASCADE;
CREATE TABLE users_authorities (
    user_id BIGINT NOT NULL,
    authority_name VARCHAR(50) NOT NULL
);

------------------------- INDICES -------------------------

-- _groups
CREATE UNIQUE INDEX pk_groups ON _groups (id ASC);
CREATE INDEX ux_groups_users_id ON _groups (user_id ASC);

-- subjects
CREATE UNIQUE INDEX pk_subjects ON subjects (id ASC);
CREATE INDEX ux_subjects_users_id ON subjects (user_id ASC);

-- groups_subjects
CREATE UNIQUE INDEX pk_groups_subjects ON groups_subjects (subjects_id ASC, groups_id ASC);

-- boards
CREATE UNIQUE INDEX pk_boards ON boards (id ASC);
CREATE INDEX ux_boards_groups_id ON boards (group_id ASC);
CREATE INDEX ux_boards_subjects_id ON boards (subject_id ASC);
CREATE INDEX ux_boards_users_id ON boards (user_id ASC);

-- day_types
CREATE UNIQUE INDEX pk_day_types ON day_types (id ASC);

-- students
CREATE UNIQUE INDEX pk_students ON students (id ASC);

-- days
CREATE UNIQUE INDEX pk_days ON days (id ASC);
CREATE INDEX ux_days_day_types_id ON days (day_type_id ASC);

-- authorities
CREATE UNIQUE INDEX pk_authorities ON authorities (name ASC);

-- users
CREATE UNIQUE INDEX pk_users ON users (id ASC);
CREATE UNIQUE INDEX ux_users_email ON users (email ASC);
CREATE UNIQUE INDEX ux_users_login ON users (login ASC);

-- users_authorities
CREATE UNIQUE INDEX pk_users_authorities ON users_authorities (user_id ASC, authority_name ASC);

------------------------- CONSTRAINTS -------------------------

-- _groups
ALTER TABLE _groups ADD CONSTRAINT fk_groups_user_id FOREIGN KEY (user_id) REFERENCES users (id);

-- subjects
ALTER TABLE subjects ADD CONSTRAINT fk_subjects_user_id FOREIGN KEY (user_id) REFERENCES users (id);

-- boards
ALTER TABLE boards ADD CONSTRAINT fk_boards_group_id FOREIGN KEY (group_id) REFERENCES _groups (id);
ALTER TABLE boards ADD CONSTRAINT fk_boards_subject_id FOREIGN KEY (subject_id) REFERENCES subjects (id);
ALTER TABLE boards ADD CONSTRAINT fk_boards_user_id FOREIGN KEY (user_id) REFERENCES users (id);

-- day_types
ALTER TABLE day_types ADD CONSTRAINT fk_day_types_board_id FOREIGN KEY (board_id) REFERENCES boards (id);

-- students
ALTER TABLE students ADD CONSTRAINT fk_students_group_id FOREIGN KEY (group_id) REFERENCES _groups (id);

-- days
ALTER TABLE days ADD CONSTRAINT fk_days_day_type_id FOREIGN KEY (day_type_id) REFERENCES day_types (id);
ALTER TABLE days ADD CONSTRAINT fk_days_student_id FOREIGN KEY (student_id) REFERENCES students (id);

-- users_authorities
ALTER TABLE users_authorities ADD CONSTRAINT pkey_users_authorities PRIMARY KEY (user_id, authority_name);
ALTER TABLE users_authorities ADD CONSTRAINT fk_authority_name FOREIGN KEY (authority_name) REFERENCES authorities (name);
ALTER TABLE users_authorities ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users (id);

------------------------- INSERT DATA -------------------------

-- users
INSERT INTO users (login, password_hash, first_name, last_name, email, image_url, activated, lang_key, activation_key, reset_key, created_by, created, reset_date, updated_by, updated) VALUES
    ('system', '$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG', 'System', 'System', 'system@localhost', '', TRUE, 'ua', NULL, NULL, 'system', NULL, NULL, 'system', NULL),
    ('anonymoususer', '$2a$10$j8S5d7Sr7.8VTOYNviDPOeWX8KcYILUVJBsYV83Y5NtECayypx9lO', 'Anonymous', 'System', 'anonymous@localhost', '', TRUE, 'ua', NULL, NULL, 'system', NULL, NULL, 'system', NULL),
    ('admin', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', 'System', 'Administrator', 'admin@localhost', '', TRUE, 'ua', NULL, NULL, 'system', NULL, NULL, 'system', NULL),
    ('user', '$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K', 'System', 'User', 'user@localhost', '', TRUE, 'ua', NULL, NULL, 'system', NULL, NULL, 'system', NULL),
    ('teacher', '$2a$10$ikrj7VVeyrPAcEtX4GAHquITr1EOwQGFsVYNveNIl0HcuoWEriqpa', 'System', 'Teacher', 'teacher@localhost', '', TRUE, 'ua', NULL, NULL, 'system', NULL, NULL, 'system', NULL);

-- authorities
INSERT INTO authorities (name) VALUES
    ('ROLE_ADMIN'), ('ROLE_USER');

-- users_authorities
INSERT INTO users_authorities (user_id, authority_name) VALUES
    (1, 'ROLE_ADMIN'), (1, 'ROLE_USER'), (3, 'ROLE_ADMIN'), (3, 'ROLE_USER'), (4, 'ROLE_USER'), (5, 'ROLE_ADMIN'), (5, 'ROLE_USER');

-- _groups
INSERT INTO _groups (name, description, created_by, created, updated_by, updated, user_id) VALUES
    /*1*/   ('Інформатики-4', 'Тестова група для перевірки роботи розумного журналу. Пишу багато символів в описі, щоб перевірити, як це буде виглядати на сторінці. Ліміт сиволів - 65535, тому в мене ще є великий запас. Бла-бла-бла...', 'system', CURRENT_TIMESTAMP(3), NULL, NULL, 3),
    /*2*/   ('Прикладна математика-3', NULL, 'system', CURRENT_TIMESTAMP(3), 'system', CURRENT_TIMESTAMP(3), 3),
    /*3*/   ('Прикладна математика-4', NULL, 'system', CURRENT_TIMESTAMP(3), 'system', CURRENT_TIMESTAMP(3), 3),
    /*4*/   ('Група 2-го викладача', NULL, 'system', CURRENT_TIMESTAMP(3), 'system', CURRENT_TIMESTAMP(3), 5),
    /*5*/   ('Комп’ютерні науки', NULL, 'system', CURRENT_TIMESTAMP(3), 'system', CURRENT_TIMESTAMP(3), 3);

-- subjects
INSERT INTO subjects (name, description, created_by, created, updated_by, updated, user_id) VALUES
    /*1*/    ('Програмна інженерія', NULL, 'system', CURRENT_TIMESTAMP(3), 'system', CURRENT_TIMESTAMP(3), 3),
    /*2*/    ('Системне програмування', 'Системне програмування та операційні системи', 'system', CURRENT_TIMESTAMP(3), 'system', CURRENT_TIMESTAMP(3), 3),
    /*3*/    ('Предмет 2-го викладача', NULL, 'system', CURRENT_TIMESTAMP(3), 'system', CURRENT_TIMESTAMP(3), 5),
    /*4*/    ('Математичний аналіз', NULL, 'system', CURRENT_TIMESTAMP(3), 'system', CURRENT_TIMESTAMP(3), 3);

-- groups_subjects
INSERT INTO groups_subjects (groups_id, subjects_id) VALUES
    (1, 1), (1, 2), (2, 1), (3, 1), (4, 3);

-- boards
INSERT INTO boards (title, name, description, total_score, created_by, created, updated_by, updated, group_id, subject_id, user_id) VALUES
    /*1*/    ('Інформатики-4. Програмна інженерія', '1-й семестр', '', 100.0, 'system', CURRENT_TIMESTAMP(3), 'system', CURRENT_TIMESTAMP(3), 1, 1, 3),
    /*2*/    ('Інформатики-4. Системне програмування', NULL, NULL, 100.0, 'system', CURRENT_TIMESTAMP(3), 'system', CURRENT_TIMESTAMP(3), 1, 2, 3),
    /*3*/    ('Прикладна математика-3. Програмна інженерія', '2-й семестр', 'Згенерований журнал для групи "Прикладна математика-3" і предмету "Програмна інженерія"', 1000.0, 'system', CURRENT_TIMESTAMP(3), 'system', CURRENT_TIMESTAMP(3), 2, 1, 3),
    /*4*/    ('Прикладна математика-4. Програмна інженерія', NULL, 'Останній семестр для групи "Прикладна математика-4" з дисципліни "Програмна інженерія"', NULL, 'system', CURRENT_TIMESTAMP(3), 'system', CURRENT_TIMESTAMP(3), 3, 1, 3),
    /*5*/    ('Дошка 2-го викладача', NULL, NULL, NULL, 'system', CURRENT_TIMESTAMP(3), 'system', CURRENT_TIMESTAMP(3), 4, 3, 5);

-- day_types
INSERT INTO day_types (type, score, name, description, expiry, created_by, created, updated_by, updated, board_id) VALUES
    /*1*/   ('SIMPLE', 1.0, 'Відвідування', 'Звичайна пара', NULL, 'system', CURRENT_TIMESTAMP(3), 'system', CURRENT_TIMESTAMP(3), 1),
    /*2*/   ('LAB', 5.0, 'Лабараторна робота', NULL, 3, 'system', CURRENT_TIMESTAMP(3), 'system', CURRENT_TIMESTAMP(3), 1),
    /*3*/   ('MODULE', 10.0, 'Модуль', NULL, NULL, 'system', CURRENT_TIMESTAMP(3), 'system', CURRENT_TIMESTAMP(3), 1),
    /*4*/   ('EXAM', 20.0, 'Екзамен', NULL, NULL, 'system', CURRENT_TIMESTAMP(3), 'system', CURRENT_TIMESTAMP(3), 1),
    /*5*/   ('TEST', 3.0, 'Тест', NULL, NULL, 'system', CURRENT_TIMESTAMP(3), 'system', CURRENT_TIMESTAMP(3), 1),
    --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /*6*/   ('SIMPLE', 1.5, 'Пара', 'Звичайна пара, відвідування якої оцінюється', NULL, 'system', CURRENT_TIMESTAMP(3), 'system', CURRENT_TIMESTAMP(3), 2),
    /*7*/   ('LAB', 8.0, 'Лабараторна робота №1. Багатопоточність в прикладних системах', NULL, 5, 'system', CURRENT_TIMESTAMP(3), 'system', CURRENT_TIMESTAMP(3), 2),
    /*8*/   ('LAB', 5.0, 'Лабараторна робота №2. Задача про обідаючих філософів', NULL, 5, 'system', CURRENT_TIMESTAMP(3), 'system', CURRENT_TIMESTAMP(3), 2),
    /*9*/   ('LAB', 5.0, 'Лабараторна робота №3. XML-парсер', NULL, 5, 'system', CURRENT_TIMESTAMP(3), 'system', CURRENT_TIMESTAMP(3), 2),
    /*10*/   ('LAB', 7.0, 'Лабараторна робота №4. Імітатор шедулера операційної системи', NULL, 5, 'system', CURRENT_TIMESTAMP(3), 'system', CURRENT_TIMESTAMP(3), 2),
    /*11*/   ('LAB', 10.0, 'Лабараторна робота №5. Генетичні алгоритми', NULL, 5, 'system', CURRENT_TIMESTAMP(3), 'system', CURRENT_TIMESTAMP(3), 2),
    /*12*/   ('MODULE', 15.0, 'Підсумковий модуль №1. Багатопоточність', NULL, 5, 'system', CURRENT_TIMESTAMP(3), 'system', CURRENT_TIMESTAMP(3), 2),
    /*13*/   ('MODULE', 15.0, 'Підсумковий модуль №2. Прикладні програми в системному програмуванні', NULL, NULL, 'system', CURRENT_TIMESTAMP(3), 'system', CURRENT_TIMESTAMP(3), 2),
    /*14*/   ('EXAM', 20.0, 'Семестровий екзамен', NULL, NULL, 'system', CURRENT_TIMESTAMP(3), 'system', CURRENT_TIMESTAMP(3), 2),
    --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /*15*/   ('SIMPLE', 2.0, 'Пара', 'Звичайна пара, відвідування якої оцінюється', NULL, 'system', CURRENT_TIMESTAMP(3), 'system', CURRENT_TIMESTAMP(3), 3),
    --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /*16*/   ('SIMPLE', 1.0, 'Пара', 'Звичайна пара, відвідування якої оцінюється', NULL, 'system', CURRENT_TIMESTAMP(3), 'system', CURRENT_TIMESTAMP(3), 4),
    --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /*17*/   ('SIMPLE', 0.0, 'Звичайна пара', 'Звичайна пара, відвідування якої НЕ оцінюється', NULL, 'system', CURRENT_TIMESTAMP(3), 'system', CURRENT_TIMESTAMP(3), 5);

-- students
INSERT INTO students (first_name, last_name, middle_name, group_id) VALUES
    /*1*/   ('Антон', 'Яковенко', 'Сергійович', 1),
    /*2*/   ('Артем', 'Яковенко', 'Сергійович', 1),
    /*3*/   ('Джейсон', 'Стетхем', NULL, 1),
    /*4*/   ('Довге-довге ім’я', 'Довге-довге прізвище', 'Довге-довге ім’я по-батькові', 1),
    /*5*/   ('Семен', 'Швидкий', 'Вікторович', 1),
    /*6*/   ('Поліна', 'Іванова', 'Ігорівна', 1),
    /*7*/   ('Олександра', 'Таращук', 'Олексіївна', 1),
    /*8*/   ('Катерина', 'Микитюк', 'Борисівна', 1),
    /*9*/   ('Світлана', 'Шинкаренко', 'Тарасівна', 1),
    /*10*/   ('Юлія', 'Крамаренко', 'Олександрівна', 1),
    --------------------------------------------------------
    /*11*/   ('Олександр', 'Антоненко', 'Володимирович', 2),
    /*12*/   ('Любов', 'Петренко', 'Анатоліївна', 2),
    /*13*/   ('Адам', 'Іванченко', 'Борисович', 2),
    /*14*/   ('Діана', 'Лисенко', 'Сергіївна', 2),
    /*15*/   ('Віталій', 'Петренко', 'Олексійович', 2),
    /*16*/   ('Ніна', 'Шевченко', 'Андріївна', 2),
    /*17*/   ('Юрій', 'Романченко', 'Євгенович', 2),
    /*18*/   ('Оксана', 'Лисенко', 'Михайлівна', 2),
    --------------------------------------------------------
    /*19*/   ('Оксана', 'Броваренко', 'Петрівна', 3),
    /*20*/   ('Ольга', 'Мірошниченко', 'Романівна', 3),
    /*21*/   ('Тетяна', 'Мельниченко', 'Андріївна', 3),
    /*22*/   ('Дмитро', 'Крамарчук', 'Олексійович', 3),
    /*23*/   ('Софія', 'Іванченко', 'Петрівна', 3),
    /*24*/   ('B''ячеслав', 'Сергієнко', 'Петрович', 3),
    /*25*/   ('Павло', 'Янович', 'Антоненко', 3),
    /*26*/   ('Валерій', 'Овсієнко', 'Федорович', 3),
    --------------------------------------------------------
    /*27*/   ('Олексій', 'Трохименко', NULL, 4);

-- days (board with id = 1)
INSERT INTO days (date, result, student_id, day_type_id) VALUES
    (CURRENT_DATE - integer '28', 0.0, 1, 1),
    (CURRENT_DATE - integer '28', 1.0, 2, 1),
    (CURRENT_DATE - integer '28', 0.0, 3, 1),
    (CURRENT_DATE - integer '28', 1.0, 4, 1),
    (CURRENT_DATE - integer '28', 1.0, 5, 1),
    (CURRENT_DATE - integer '28', 1.0, 6, 1),
    (CURRENT_DATE - integer '28', 1.0, 7, 1),
    (CURRENT_DATE - integer '28', 1.0, 8, 1),
    (CURRENT_DATE - integer '28', 0.0, 9, 1),
    (CURRENT_DATE - integer '28', 1.0, 10, 1),
    ----------------------------------------
    (CURRENT_DATE - integer '21', 1.0, 1, 2),
    (CURRENT_DATE - integer '21', 2.0, 2, 2),
    (CURRENT_DATE - integer '21', 3.0, 3, 2),
    (CURRENT_DATE - integer '21', 4.0, 4, 2),
    (CURRENT_DATE - integer '21', 0.0, 5, 2),
    (CURRENT_DATE - integer '21', 4.0, 6, 2),
    (CURRENT_DATE - integer '21', 5.0, 7, 2),
    (CURRENT_DATE - integer '21', 5.0, 8, 2),
    (CURRENT_DATE - integer '21', 0.0, 9, 2),
    (CURRENT_DATE - integer '21', 2.0, 10, 2),
    ----------------------------------------
    (CURRENT_DATE - integer '14', 0.0, 1, 1),
    (CURRENT_DATE - integer '14', 1.0, 2, 1),
    (CURRENT_DATE - integer '14', 1.0, 3, 1),
    (CURRENT_DATE - integer '14', 0.0, 4, 1),
    (CURRENT_DATE - integer '14', 1.0, 5, 1),
    (CURRENT_DATE - integer '14', 0.0, 6, 1),
    (CURRENT_DATE - integer '14', 0.0, 7, 1),
    (CURRENT_DATE - integer '14', 1.0, 8, 1),
    (CURRENT_DATE - integer '14', 0.0, 9, 1),
    (CURRENT_DATE - integer '14', 0.0, 10, 1),
    ----------------------------------------
    (CURRENT_DATE - integer '7', 0.0, 1, 1),
    (CURRENT_DATE - integer '7', 1.0, 2, 1),
    (CURRENT_DATE - integer '7', 1.0, 3, 1),
    (CURRENT_DATE - integer '7', 0.0, 4, 1),
    (CURRENT_DATE - integer '7', 0.0, 5, 1),
    (CURRENT_DATE - integer '7', 1.0, 6, 1),
    (CURRENT_DATE - integer '7', 1.0, 7, 1),
    (CURRENT_DATE - integer '7', 1.0, 8, 1),
    (CURRENT_DATE - integer '7', 1.0, 9, 1),
    (CURRENT_DATE - integer '7', 1.0, 10, 1),
    ----------------------------------------
    (CURRENT_DATE, NULL, 1, 5),
    (CURRENT_DATE, NULL, 2, 5),
    (CURRENT_DATE, NULL, 3, 5),
    (CURRENT_DATE, NULL, 4, 5),
    (CURRENT_DATE, NULL, 5, 5),
    (CURRENT_DATE, NULL, 6, 5),
    (CURRENT_DATE, NULL, 7, 5),
    (CURRENT_DATE, NULL, 8, 5),
    (CURRENT_DATE, NULL, 9, 5),
    (CURRENT_DATE, NULL, 10, 5),
    ----------------------------------------
    (CURRENT_DATE + integer '7', NULL, 1, 1),
    (CURRENT_DATE + integer '7', NULL, 2, 1),
    (CURRENT_DATE + integer '7', NULL, 3, 1),
    (CURRENT_DATE + integer '7', NULL, 4, 1),
    (CURRENT_DATE + integer '7', NULL, 5, 1),
    (CURRENT_DATE + integer '7', NULL, 6, 1),
    (CURRENT_DATE + integer '7', NULL, 7, 1),
    (CURRENT_DATE + integer '7', NULL, 8, 1),
    (CURRENT_DATE + integer '7', NULL, 9, 1),
    (CURRENT_DATE + integer '7', NULL, 10, 1),
    ----------------------------------------
    (CURRENT_DATE + integer '14', NULL, 1, 3),
    (CURRENT_DATE + integer '14', NULL, 2, 3),
    (CURRENT_DATE + integer '14', NULL, 3, 3),
    (CURRENT_DATE + integer '14', NULL, 4, 3),
    (CURRENT_DATE + integer '14', NULL, 5, 3),
    (CURRENT_DATE + integer '14', NULL, 6, 3),
    (CURRENT_DATE + integer '14', NULL, 7, 3),
    (CURRENT_DATE + integer '14', NULL, 8, 3),
    (CURRENT_DATE + integer '14', NULL, 9, 3),
    (CURRENT_DATE + integer '14', NULL, 10, 3),
    ----------------------------------------
    (CURRENT_DATE + integer '21', NULL, 1, 1),
    (CURRENT_DATE + integer '21', NULL, 2, 1),
    (CURRENT_DATE + integer '21', NULL, 3, 1),
    (CURRENT_DATE + integer '21', NULL, 4, 1),
    (CURRENT_DATE + integer '21', NULL, 5, 1),
    (CURRENT_DATE + integer '21', NULL, 6, 1),
    (CURRENT_DATE + integer '21', NULL, 7, 1),
    (CURRENT_DATE + integer '21', NULL, 8, 1),
    (CURRENT_DATE + integer '21', NULL, 9, 1),
    (CURRENT_DATE + integer '21', NULL, 10, 1),
    ----------------------------------------
    (CURRENT_DATE + integer '28', NULL, 1, 1),
    (CURRENT_DATE + integer '28', NULL, 2, 1),
    (CURRENT_DATE + integer '28', NULL, 3, 1),
    (CURRENT_DATE + integer '28', NULL, 4, 1),
    (CURRENT_DATE + integer '28', NULL, 5, 1),
    (CURRENT_DATE + integer '28', NULL, 6, 1),
    (CURRENT_DATE + integer '28', NULL, 7, 1),
    (CURRENT_DATE + integer '28', NULL, 8, 1),
    (CURRENT_DATE + integer '28', NULL, 9, 1),
    (CURRENT_DATE + integer '28', NULL, 10, 1),
    ----------------------------------------
    (CURRENT_DATE + integer '35', NULL, 1, 1),
    (CURRENT_DATE + integer '35', NULL, 2, 1),
    (CURRENT_DATE + integer '35', NULL, 3, 1),
    (CURRENT_DATE + integer '35', NULL, 4, 1),
    (CURRENT_DATE + integer '35', NULL, 5, 1),
    (CURRENT_DATE + integer '35', NULL, 6, 1),
    (CURRENT_DATE + integer '35', NULL, 7, 1),
    (CURRENT_DATE + integer '35', NULL, 8, 1),
    (CURRENT_DATE + integer '35', NULL, 9, 1),
    (CURRENT_DATE + integer '35', NULL, 10, 1),
    ----------------------------------------
    (CURRENT_DATE + integer '42', NULL, 1, 4),
    (CURRENT_DATE + integer '42', NULL, 2, 4),
    (CURRENT_DATE + integer '42', NULL, 3, 4),
    (CURRENT_DATE + integer '42', NULL, 4, 4),
    (CURRENT_DATE + integer '42', NULL, 5, 4),
    (CURRENT_DATE + integer '42', NULL, 6, 4),
    (CURRENT_DATE + integer '42', NULL, 7, 4),
    (CURRENT_DATE + integer '42', NULL, 8, 4),
    (CURRENT_DATE + integer '42', NULL, 9, 4),
    (CURRENT_DATE + integer '42', NULL, 10, 4),
    --(board with id = 2)-------------------
    (CURRENT_DATE + integer '7', NULL, 1, 6),
    (CURRENT_DATE + integer '7', NULL, 2, 6),
    (CURRENT_DATE + integer '7', NULL, 3, 6),
    (CURRENT_DATE + integer '7', NULL, 4, 6),
    (CURRENT_DATE + integer '7', NULL, 5, 6),
    (CURRENT_DATE + integer '7', NULL, 6, 6),
    (CURRENT_DATE + integer '7', NULL, 7, 6),
    (CURRENT_DATE + integer '7', NULL, 8, 6),
    (CURRENT_DATE + integer '7', NULL, 9, 6),
    (CURRENT_DATE + integer '7', NULL, 10, 6),
    --(board with id = 3)-------------------
    (CURRENT_DATE - integer '7', 0.0, 11, 15),
    (CURRENT_DATE - integer '7', 1.0, 12, 15),
    (CURRENT_DATE - integer '7', 2.0, 13, 15),
    (CURRENT_DATE - integer '7', 0.0, 14, 15),
    (CURRENT_DATE - integer '7', 1.0, 15, 15),
    (CURRENT_DATE - integer '7', 2.0, 16, 15),
    (CURRENT_DATE - integer '7', 0.0, 17, 15),
    (CURRENT_DATE - integer '7', 1.0, 18, 15);

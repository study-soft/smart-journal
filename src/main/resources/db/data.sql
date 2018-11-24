ALTER USER postgres WITH SUPERUSER;

-- Disable triggers
ALTER TABLE party DISABLE TRIGGER ALL;
ALTER TABLE subject DISABLE TRIGGER ALL;
ALTER TABLE party_subject DISABLE TRIGGER ALL;
ALTER TABLE board DISABLE TRIGGER ALL;
ALTER TABLE day_type DISABLE TRIGGER ALL;
ALTER TABLE student DISABLE TRIGGER ALL;
ALTER TABLE day DISABLE TRIGGER ALL;
ALTER TABLE jhi_authority DISABLE TRIGGER ALL;
ALTER TABLE jhi_persistent_audit_event DISABLE TRIGGER ALL;
ALTER TABLE jhi_persistent_audit_evt_data DISABLE TRIGGER ALL;
ALTER TABLE jhi_user DISABLE TRIGGER ALL;
ALTER TABLE jhi_user_authority DISABLE TRIGGER ALL;

-- Clear test data
TRUNCATE TABLE party CASCADE;
TRUNCATE TABLE subject CASCADE;
TRUNCATE TABLE party_subject CASCADE;
TRUNCATE TABLE board CASCADE;
TRUNCATE TABLE day_type CASCADE;
TRUNCATE TABLE student CASCADE;
TRUNCATE TABLE day CASCADE;
TRUNCATE TABLE jhi_authority CASCADE;
TRUNCATE TABLE jhi_persistent_audit_event CASCADE;
TRUNCATE TABLE jhi_persistent_audit_evt_data CASCADE;
TRUNCATE TABLE jhi_user CASCADE;
TRUNCATE TABLE jhi_user_authority CASCADE;

-- Reset serials
ALTER SEQUENCE party_id_seq RESTART WITH 1;
ALTER SEQUENCE subject_id_seq RESTART WITH 1;
ALTER SEQUENCE day_type_id_seq RESTART WITH 1;
ALTER SEQUENCE student_id_seq RESTART WITH 1;
ALTER SEQUENCE day_id_seq RESTART WITH 1;
ALTER SEQUENCE jhi_persistent_audit_event_event_id_seq RESTART WITH 1;
ALTER SEQUENCE jhi_user_id_seq RESTART WITH 1;

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

-- set current serial to max + 1
SELECT setval('party_id_seq', COALESCE((SELECT MAX(id) FROM party), 0));
SELECT setval('subject_id_seq', COALESCE((SELECT MAX(id) FROM subject), 0));
SELECT setval('day_type_id_seq', COALESCE((SELECT MAX(id) FROM day_type), 0));
SELECT setval('student_id_seq', COALESCE((SELECT MAX(id) FROM student), 0));
SELECT setval('day_id_seq', COALESCE((SELECT MAX(id) FROM day), 0));
SELECT setval('jhi_persistent_audit_event_event_id_seq', COALESCE((SELECT MAX(event_id) FROM jhi_persistent_audit_event), 0));
SELECT setval('jhi_user_id_seq', COALESCE((SELECT MAX(id) FROM jhi_user), 0));

-- Enable triggers
ALTER TABLE party ENABLE TRIGGER ALL;
ALTER TABLE subject ENABLE TRIGGER ALL;
ALTER TABLE party_subject ENABLE TRIGGER ALL;
ALTER TABLE board ENABLE TRIGGER ALL;
ALTER TABLE day_type ENABLE TRIGGER ALL;
ALTER TABLE student ENABLE TRIGGER ALL;
ALTER TABLE day ENABLE TRIGGER ALL;
ALTER TABLE jhi_authority ENABLE TRIGGER ALL;
ALTER TABLE jhi_persistent_audit_event ENABLE TRIGGER ALL;
ALTER TABLE jhi_persistent_audit_evt_data ENABLE TRIGGER ALL;
ALTER TABLE jhi_user ENABLE TRIGGER ALL;
ALTER TABLE jhi_user_authority ENABLE TRIGGER ALL;

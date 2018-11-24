------------------------- tables -------------------------

-- party
DROP TABLE IF EXISTS party CASCADE;
CREATE TABLE party (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    description VARCHAR(65535),
    created TIMESTAMP NOT NULL,
    updated TIMESTAMP NOT NULL
);

-- subject
DROP TABLE IF EXISTS subject CASCADE;
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

------------------------- indices -------------------------

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

------------------------- keys -------------------------

-- party_subject
ALTER TABLE party_subject ADD CONSTRAINT pkey_party_subject PRIMARY KEY (subjects_id, parties_id);

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

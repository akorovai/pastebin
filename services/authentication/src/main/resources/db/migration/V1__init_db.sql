
CREATE SEQUENCE IF NOT EXISTS _user_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS role_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS token_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE _user
(
    id                 INTEGER      NOT NULL,
    nickname           VARCHAR(255) NOT NULL,
    password           VARCHAR(255),
    created_date       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk__user PRIMARY KEY (id)
);

CREATE TABLE _user_roles
(
    roles_id INTEGER NOT NULL,
    user_id  INTEGER NOT NULL
);

CREATE TABLE role
(
    id                 INTEGER NOT NULL,
    name               VARCHAR(255),
    created_date       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_role PRIMARY KEY (id)
);

CREATE TABLE token
(
    id           INTEGER NOT NULL,
    token        VARCHAR(255),
    created_at   TIMESTAMP WITHOUT TIME ZONE,
    expires_at   TIMESTAMP WITHOUT TIME ZONE,
    validated_at TIMESTAMP WITHOUT TIME ZONE,
    user_id      INTEGER NOT NULL,
    CONSTRAINT pk_token PRIMARY KEY (id)
);

ALTER TABLE _user
    ADD CONSTRAINT uc__user_nickname UNIQUE (nickname);

ALTER TABLE role
    ADD CONSTRAINT uc_role_name UNIQUE (name);

ALTER TABLE token
    ADD CONSTRAINT uc_token_token UNIQUE (token);

ALTER TABLE token
    ADD CONSTRAINT FK_TOKEN_ON_USERID FOREIGN KEY (user_id) REFERENCES _user (id);

ALTER TABLE _user_roles
    ADD CONSTRAINT fk_userol_on_role FOREIGN KEY (roles_id) REFERENCES role (id);

ALTER TABLE _user_roles
    ADD CONSTRAINT fk_userol_on_user FOREIGN KEY (user_id) REFERENCES _user (id);
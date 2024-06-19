CREATE TYPE task_status AS ENUM ('draft', 'ready_for_approval', 'approved');
CREATE TYPE submission_mode AS ENUM ('run', 'diagnose', 'submit');

CREATE CAST (CHARACTER VARYING as task_status) WITH INOUT AS IMPLICIT;
CREATE CAST (CHARACTER VARYING as submission_mode) WITH INOUT AS IMPLICIT;


CREATE TABLE umlTask
(
    id            BIGINT        NOT NULL,
    complete_comparison BOOLEAN NOT NULL,
    max_points    NUMERIC(7, 2) NOT NULL,
    status        TASK_STATUS   NOT NULL,
    identifiers   TEXT[]       ,
    classPoints    NUMERIC(7, 2) NOT NULL,
    attributePoints    NUMERIC(7, 2) NOT NULL,
    relationshipPoints    NUMERIC(7, 2) NOT NULL,
    constraintPoints    NUMERIC(7, 2) NOT NULL,
    associationPoints    NUMERIC(7, 2) NOT NULL,
    CONSTRAINT task_pk PRIMARY KEY (id)
);

CREATE TABLE umlBlock
(
    id             integer generated always as identity primary key,
    task_id       BIGINT        NOT NULL,
    CONSTRAINT solution_task_fk FOREIGN KEY (task_id) REFERENCES umlTask (id)
);

CREATE TABLE umlBlockAlt
(
    id             integer generated always as identity primary key,
    uml_block_alternative    TEXT          NOT NULL,
    uml_block_id       integer         NOT NULL,
    CONSTRAINT solution_alternative_solution_fk FOREIGN KEY (uml_block_id) REFERENCES umlBlock (id)
);

CREATE TABLE submission
(
    id                UUID                     DEFAULT gen_random_uuid(),
    user_id           VARCHAR(255),
    assignment_id     VARCHAR(255),
    task_id           BIGINT,
    submission_time   TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    language          VARCHAR(2)      NOT NULL DEFAULT 'en',
    mode              submission_mode NOT NULL,
    feedback_level    INT             NOT NULL,
    evaluation_result JSONB,
    submission        TEXT, -- custom column
    CONSTRAINT submission_pk PRIMARY KEY (id),
    CONSTRAINT submission_task_fk FOREIGN KEY (task_id) REFERENCES umlTask (id)
        ON DELETE CASCADE
);

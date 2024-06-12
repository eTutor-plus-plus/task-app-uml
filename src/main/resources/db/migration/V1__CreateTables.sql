CREATE TYPE task_status AS ENUM ('draft', 'ready_for_approval', 'approved');
CREATE TYPE submission_mode AS ENUM ('run', 'diagnose', 'submit');

CREATE CAST (CHARACTER VARYING as task_status) WITH INOUT AS IMPLICIT;
CREATE CAST (CHARACTER VARYING as submission_mode) WITH INOUT AS IMPLICIT;


CREATE TABLE task
(
    id            BIGINT        NOT NULL,
    max_points    NUMERIC(7, 2) NOT NULL,
    status        TASK_STATUS   NOT NULL,
    identifiers   TEXT       NOT NULL,
    CONSTRAINT task_pk PRIMARY KEY (id)
);

CREATE TABLE solutionBlock
(
    id            BIGINT        NOT NULL,
    task_id       BIGINT        NOT NULL,
    CONSTRAINT solution_pk PRIMARY KEY (id),
    CONSTRAINT solution_task_fk FOREIGN KEY (task_id) REFERENCES task (id)
);

CREATE TABLE solutionBlockAlternative
(
    id            BIGINT        NOT NULL,
    solution_block_alternative    TEXT          NOT NULL,
    solution_block_id       BIGINT        NOT NULL,
    CONSTRAINT solution_alternative_pk PRIMARY KEY (id),
    CONSTRAINT solution_alternative_solution_fk FOREIGN KEY (solution_block_id) REFERENCES solutionBlock (id)
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
    CONSTRAINT submission_task_fk FOREIGN KEY (task_id) REFERENCES task (id)
        ON DELETE CASCADE
);

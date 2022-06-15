CREATE TYPE entry_location AS ENUM (
    'inbox',
    'waiting_for',
    'some_day',
    'archived',
    'project'
    );

CREATE TYPE entry_state AS ENUM (
    'done',
    'todo'
    );

CREATE TABLE entry
(
    id       INT PRIMARY KEY,
    content  TEXT         NOT NULL DEFAULT '',
    location text         NOT NULL,
    context  VARCHAR(256) NULL,
    state    entry_state  NOT NULL
);
--
CREATE TABLE project
(
    id   INT PRIMARY KEY,
    name TEXT NOT NULL DEFAULT ''
);

-- seed some data
-- entry
INSERT INTO entry (id, content, location, context, state)
VALUES (1, 'entry 1', 'inbox', '@seed', 'todo'),
       (2, 'entry 2', 'some_day', '@seed', 'todo'),
       (3, 'entry 3', 'waiting_for', '@seed', 'done'),
       (4, 'entry 4', 'archived', '@seed', 'todo')
;
-- project
INSERT INTO project (id, name)
VALUES (1, 'project 1'),
       (2, 'project 2'),
       (3, 'project 3'),
       (4, 'project 4')
;

CREATE TYPE entry_location AS ENUM (
    'inbox',
    'waiting_for',
    'some_day',
    'archived',
    'project'
);
--;;
CREATE TYPE entry_state AS ENUM ('done', 'todo');
--;;
CREATE TABLE entry (
    id INT PRIMARY KEY,
    content TEXT NOT NULL DEFAULT '',
    location text NOT NULL,
    context VARCHAR(256) NULL,
    state entry_state NOT NULL
);
--;;
CREATE TABLE project (
    id INT PRIMARY KEY,
    name TEXT NOT NULL DEFAULT ''
);
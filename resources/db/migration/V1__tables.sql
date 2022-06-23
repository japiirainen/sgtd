CREATE TYPE entry_location AS ENUM (
    'inbox',
    'waiting-for',
    'some-day',
    'archived',
    'project'
);
--;;
CREATE TYPE entry_state AS ENUM ('done', 'todo');
--;;
CREATE TABLE entry (
    id SERIAL PRIMARY KEY,
    content TEXT NOT NULL DEFAULT '',
    location VARCHAR(56) NOT NULL,
    context VARCHAR(256) NULL,
    state VARCHAR(56) NOT NULL
);
--;;
CREATE TABLE project (
    id INT PRIMARY KEY,
    name TEXT NOT NULL DEFAULT ''
);

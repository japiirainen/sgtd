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

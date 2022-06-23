-- :name sql-get-entries :? :*
-- :doc Get all entries
SELECT *
FROM entry;

-- :name sql-get-entry :? :1
-- :doc Get an entry by id
SELECT *
FROM entry
WHERE id = :id;

-- :name sql-insert-entry :<! :1
-- :doc Insert an entry
INSERT INTO entry
(content, location, context, state)
VALUES
(:content, :location, :context, :state)
RETURNING *;

-- :name sql-remove-entries :!
-- :doc Delete all entries
TRUNCATE entry;

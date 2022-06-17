-- :name get-entry-by-id
-- :doc returns an entry by id, or nil if not present
SELECT *
FROM entry
WHERE id = :id

-- :name get-entries
-- :doc get all entries
SELECT * FROM entry

-- :name get-project-by-id
-- :doc returns a project by id, or nil if not present
SELECT *
FROM project
WHERE id = :id

-- :name get-projects
-- :doc get all projects
SELECT * FROM project
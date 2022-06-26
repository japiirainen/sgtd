(ns sgtd.entry.entry-db
  (:require [hugsql.core :as hugsql]))

(declare sql-get-entries)
(declare sql-get-entry)
(declare sql-insert-entry)
(declare sql-remove-entries)
(hugsql/def-db-fns "sgtd/entry/entry.sql" {:quoting :ansi})

(defn get-entries [db]
  (->> (sql-get-entries db) vec))

(defn get-entry [db id]
  (sql-get-entry db {:id id}))

(defn insert-entry! [db entry]
  (sql-insert-entry db entry))

(defn remove-entries! [db]
  (sql-remove-entries db)
  nil)

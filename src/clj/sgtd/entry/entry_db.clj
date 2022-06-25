(ns sgtd.entry.entry-db
  (:require [hugsql.core :as hugsql]
            [sgtd.db :as db]))

(declare sql-get-entries)
(declare sql-get-entry)
(declare sql-insert-entry)
(declare sql-remove-entries)
(hugsql/def-db-fns "sgtd/entry/entry.sql" {:quoting :ansi})

(def ->db #(update % :origin db/->json))
(def <-db #(update % :origin db/<-json))

(defn get-entries [db]
  (mapv <-db (sql-get-entries db)))

(defn get-entry [db id]
  (some-> (sql-get-entry db {:id id}) (<-db)))

(defn insert-entry! [db entry]
  (some->> (update entry :id identity)
           (->db)
           (sql-insert-entry db)
           (<-db)))

(defn remove-entries! [db]
  (sql-remove-entries db)
  nil)

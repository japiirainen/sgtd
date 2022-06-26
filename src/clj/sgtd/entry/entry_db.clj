(ns sgtd.entry.entry-db
  (:require [hugsql.core :as hugsql]
            [clojure.string :as s]))

(declare sql-get-entries)
(declare sql-get-entry)
(declare sql-insert-entry)
(declare sql-remove-entries)
(hugsql/def-db-fns "sgtd/entry/entry.sql" {:quoting :ansi})

(def ->kw (fn [ns v] (->> (str ns "/" v) keyword)))
(def <-kw (partial name))

(defn ->db [entry]
  (-> entry
      (update :location (partial ->kw "entry_location"))
      (update :state (partial ->kw "entry-state"))))

(defn <-db [entry]
  (-> entry
      (update :location <-kw)
      (update :state <-kw)))

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

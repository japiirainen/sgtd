(ns user
  (:require [integrant.repl :refer [clear go halt prep init reset reset-all]]
            [integrant.repl.state :as state]
            [sgtd.main :as main]
            [sgtd.entry.entry-db :refer [insert-entry!
                                         get-entries
                                         get-entry
                                         remove-entries!]]))

(integrant.repl/set-prep! main/system-config)

(defn system [] (or state/system (throw (ex-info "System not running" {}))))

(defn env [] (::main/env (system)))
(defn db [] (:db (env)))

(comment
  (insert-entry! (db) {:content "hello!"
                       :location "inbox"
                       :context "@testing"
                       :state "todo"})
  (get-entries (db))
  (get-entry (db) 1)
  (remove-entries! (db))

  (+ 1 1)
  (prep)
  (go)
  (halt)
  (init)
  (reset)
  (reset-all)
  (clear))

(ns sgtd.entry.entry
  (:require [malli.util :as mu]
            [malli.core :as m]))

(def EntryLocation
  [:enum {:error-message "Not a valid entry-location."}
   :entry-location/inbox
   :entry-location/waiting-for
   :entry-location/some-day
   :entry-location/archived
   :entry-location/project])

(def EntryState
  [:enum {:error-message "Not a valid entry-state."}
   :entry-state/todo
   :entry-state/done])

(def Entry
  [:map {:title "entry"}
   [:id int?]
   [:content string?]
   [:location EntryLocation]
   [:context string?]
   [:state EntryState]])

(def NewEntry
  (mu/dissoc Entry :id))

(comment
  (m/explain EntryLocation :entry-location/inbox)
  (m/explain EntryLocation "something invalid")

  (m/explain EntryState :entry-state/todo)
  (m/explain EntryState "something invalid"))

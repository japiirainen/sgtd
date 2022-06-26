(ns sgtd.entry.entry
  (:require [malli.util :as mu]
            [malli.core :as m]))

(def EntryLocation
  [:enum {:error-message "Not a valid entry-location."}
   "inbox"
   "waiting-for"
   "some-day"
   "archived"
   "project"])

(def EntryState
  [:enum {:error-message "Not a valid entry-state."}
   "todo"
   "done"])

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
  (m/explain EntryLocation "inbox")
  (m/explain EntryLocation "something invalid")

  (m/explain EntryState "todo")
  (m/explain EntryState "something invalid"))

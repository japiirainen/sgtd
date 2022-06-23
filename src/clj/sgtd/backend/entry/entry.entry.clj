(ns sgtd.backend.entry.entry
  (:require [malli.util :as mu]))

(def Entry
  [:map {:title "entry"}
   [:id int?]
   [:content string?]
   [:location [:enum "inbox" "waiting-for" "some-day" "archived" "project"]]
   [:context string?]
   [:state [:enum "done" "todo"]]])

(def NewEntry
  (mu/dissoc Entry :id))

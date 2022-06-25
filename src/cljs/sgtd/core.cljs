(ns ^:figwheel-hooks sgtd.core
  (:require
   [reagent.dom :refer [render]]
   [re-frame.core :as re-frame]
   [sgtd.styles :as styles]
   [sgtd.config :as config]))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (println "mount")
  (re-frame/clear-subscription-cache!)
  (styles/inject-trace-styles js/document)
  (render [:div "Hello World!"]
          (.getElementById js/document "app")))

(defn ^:after-load re-render []
  (mount-root))

(defn ^:export init []
  (println "init again..")
  (dev-setup)

  (mount-root))
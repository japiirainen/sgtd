(ns user
  "Userspace functions you can run by default in your local REPL."
  (:require
    [clojure.pprint]
    [clojure.spec.alpha :as s]
    [clojure.tools.namespace.repl :as repl]
    [criterium.core :as c]                                  ;; benchmarking
    [expound.alpha :as expound]
    [integrant.core :as ig]
    [integrant.repl :refer [clear go halt prep init reset reset-all]]
    [integrant.repl.state :as state]
    [kit.api :as kit]
    [migratus.core :as m]
    [lambdaisland.classpath.watch-deps :as watch-deps]      ;; hot loading for deps
    [japiirainen.sgtd.core :refer [start-app]]))

;; uncomment to enable hot loading for deps
(watch-deps/start! {:aliases [:dev :test]})

(alter-var-root #'s/*explain-out* (constantly expound/printer))

(add-tap (bound-fn* clojure.pprint/pprint))

(defn dev-prep!
  []
  (integrant.repl/set-prep! (fn []
                              (-> (japiirainen.sgtd.config/system-config {:profile :dev})
                                  (ig/prep)))))

(defn test-prep!
  []
  (integrant.repl/set-prep! (fn []
                              (-> (japiirainen.sgtd.config/system-config {:profile :test})
                                  (ig/prep)))))

;; Can change this to test-prep! if want to run tests as the test profile in your repl
;; You can run tests in the dev profile, too, but there are some differences between
;; the two profiles.
(dev-prep!)

(repl/set-refresh-dirs "src/clj")

(def refresh repl/refresh)

(comment
  (+ 1 1)
  ; kit stuff
  (kit/install-module :kit/cljs)
  (kit/list-modules)
  (kit/sync-modules)

  ; migrations (migratus)
  ; create migration
  (def db-config (:db.sql/migrations state/system))
  (m/init db-config)
  (m/migrate db-config)
  (m/create db-config "initial-schema")
  (m/up db-config 20220615160556)
  (m/down db-config 20220615160556)
  (m/rollback db-config)

  (halt)

  (go)

  (reset)

  )

(ns sgtd.main
  (:require [clojure.tools.logging :as log]
            [hikari-cp.core :as hikari-cp]
            [integrant.core :as ig]
            [ring.adapter.jetty :as jetty]
            [sgtd.http :as http]
            [sgtd.routes :as routes])
  (:import [org.eclipse.jetty.server Server]
           [org.flywaydb.core Flyway]))

(defn ->long [x]
  (if (int? x) x (Long/parseLong x)))

(defn env [key default]
  (some-> (or (System/getenv (name key)) default)))

(defn system-config []
  {::db {:adapter "postgresql"
         :username (env "DB_USERNAME" "sgtd")
         :password (env "DB_PASSWORD" "sgtd")
         :server-name (env "DB_HOST" "localhost")
         :port-number (->long (env "DB_PORT" 5432))
         :database-name (env "DB_NAME" "sgtd")
         :connection-timeout 5000
         :validation-timeout 5000
         :maximum-pool-size 10}
   ::flyway {:migrate true
             ;:clean true
             :db (ig/ref ::db)}
   ::jetty {:port 3000
            :join? false
            :env (ig/ref ::env)}
   ::env {:db (ig/ref ::db)}})

(defmethod ig/init-key ::db [_ opts]
  {:datasource (hikari-cp/make-datasource opts)})

(defmethod ig/halt-key! ::db [_ this]
  (hikari-cp/close-datasource (:datasource this)))

(defmethod ig/init-key ::flyway [_ {:keys [schemas migrate clean db]}]
  (let [flyway (-> (Flyway/configure)
                   (.table "schema_version")
                   (.dataSource (:datasource db))
                   (.schemas (into-array String schemas))
                   (.locations (into-array String ["/db/migration"]))
                   (.load))]
    (when clean (.clean flyway))
    (when migrate (.migrate flyway))
    flyway))

(defmethod ig/init-key ::jetty [_ {:keys [port join? env]}]
  (-> (http/handler {:routes (routes/create env)})
      (jetty/run-jetty {:port port :join? join?})))

(defmethod ig/halt-key! ::jetty [_ ^Server server]
  (.stop server))

(defmethod ig/init-key ::env [_ env] env)

(defn -main
  "Main entrypoint for the application."
  []
  (log/info "starting...")
  (ig/init (system-config)))

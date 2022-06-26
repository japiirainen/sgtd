(ns sgtd.db
  (:require [jsonista.core :as j]
            [clojure.string :as s]
            [clojure.java.jdbc :as jdbc])
  (:import (org.postgresql.util PGobject)))

(def ^:private object-mapper (j/object-mapper {:decode-key-fn true}))

(defn ->json [m]
  (doto (PGobject.)
    (.setType "JSONB")
    (.setValue (j/write-value-as-string m))))

(defn <-json [x] (some-> x (.getValue) (j/read-value object-mapper)))
(defn <-json-str-keys [x] (some-> x (.getValue) (j/read-value)))

(defn kw->pgenum [kw]
  (let [type (-> (namespace kw)
                 (s/replace "-" "_"))
        value (name kw)]
    (doto (PGobject.)
      (.setType type)
      (.setValue value))))

(extend-type clojure.lang.Keyword
  jdbc/ISQLValue
  (sql-value [kw]
    (kw->pgenum kw)))

(def +schema-enums+
  "A set of all PostgreSQL enums in schema.sql. Used to convert
  enum-values back into Clojure keywords."
  #{"entry_location" "entry_state"})

(extend-type String
  jdbc/IResultSetReadColumn
  (result-set-read-column [val rsmeta idx]
    (let [type (.getColumnTypeName rsmeta idx)]
      (if (contains? +schema-enums+ type)
        (keyword (s/replace type "_" "-") val)
        val))))

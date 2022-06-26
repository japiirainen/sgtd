(ns sgtd.entry.entry-routes
  (:require [sgtd.entry.entry-db :as entry-db]
            [sgtd.entry.entry :as entry]
            [ring.util.http-response :as r]
            [ring.util.http-status :as status]))

(defn create [{:keys [db]}]
  ["/entry" {:swagger {:tags ["entry"]}}

   ["" {:get {:summary "list entries"
              :responses {status/ok {:body [:vector entry/Entry]}}
              :handler (fn [_]
                         (r/ok (entry-db/get-entries db)))}

        :post {:summary "add an entry"
               :parameters {:body entry/NewEntry}
               :responses {status/ok {:body entry/Entry}}
               :handler (fn [request]
                          (let [pizza (-> request :parameters :body)]
                            (r/ok (entry-db/insert-entry! db pizza))))}}]

   ["/:id" {:get {:summary "get an entry"
                  :parameters {:path [:map [:id int?]]}
                  :responses {status/ok {:body entry/Entry}
                              status/not-found {:description "not found"}}
                  :handler (fn [request]
                             (let [id (-> request :parameters :path :id)]
                               (if-let [entry (entry-db/get-entry db id)]
                                 (r/ok entry)
                                 (r/not-found))))}}]])

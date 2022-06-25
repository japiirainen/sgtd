(ns sgtd.routes
  (:require [reitit.swagger-ui :as swagger-ui]
            [reitit.swagger :as swagger]
            [fumi.client :as fc]))

(defn create [env]
  [["/swagger.json"
    {:get {:no-doc true
           :swagger {:info {:title "sgtd API"
                            :description "Simple Get Things Done API"}
                     :tags [{:name "sgtd", :description "Simple Get Things Done"}
                            {:name "dispatch" :description "dispatcher-api"}]}
           :handler (swagger/create-swagger-handler)}}]
   ["/api-docs/*"
    {:get {:no-doc true
           :handler (swagger-ui/create-swagger-ui-handler
                     {:config {:validatorUrl nil}})}}]

   ["/api"
    ["/health" {:swagger {:tags ["health"]}}
     ["/ping" {:get {:summary "ping"
                     :handler (constantly {:status 200, :body {:message "pong"}})}}]
     ["/metrics" {:get {:summary "prometheus endpoint"
                        :handler (fn [_]
                                   {:status 200,
                                    :headers {"Content-Type" "text/plain"}
                                    :body (-> (fc/collect) (fc/serialize :text))})}}]]]])

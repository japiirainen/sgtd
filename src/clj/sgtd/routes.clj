(ns sgtd.routes
  (:require [reitit.swagger-ui :as swagger-ui]
            [reitit.swagger :as swagger]
            [fumi.client :as fc]
            [sgtd.entry.entry-routes :as entry-routes]))

(defn create [env]
  [["/swagger.json"
    {:get {:no-doc true
           :swagger {:info {:title "example-api"
                            :description "Example API"}
                     :tags [{:name "pizza", :description "pizza-api"}
                            {:name "health" :description "health-api"}
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
                                    :body (-> (fc/collect) (fc/serialize :text))})}}]]

    (entry-routes/create env)

    #_["/dispatch" {:swagger {:tags ["dispatcher"]}}
       ["" {:post {:summary "generic endpoint"
                   :parameters {:body [:map
                                       [:action qualified-keyword?]
                                       [:data any?]]}
                   :responses {200 {:body any?}}
                   :handler (fn [request]
                              (let [{:keys [action data]} (-> request :parameters :body)]
                                (r/ok (d/dispatch env [action data]))))}}]
       ["/actions"
        (for [[action {:keys [type summary input output]}] d/actions]
          (let [[method parameter] (if (= :query type) [:get :query] [:post :body])]
            [(str "/" (subs (str action) 1))
             {method (cond-> {:summary summary
                              :handler (fn [request]
                                         (let [data (-> request :parameters parameter)]
                                           (r/ok (d/dispatch env [action data]))))}
                       input (assoc :parameters {parameter input})
                       output (assoc :responses {200 {:body output}}))}]))]]]])

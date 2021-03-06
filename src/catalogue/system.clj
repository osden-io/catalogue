(ns catalogue.system
  (:require [com.stuartsierra.component :as component]
            [duct.component.endpoint :refer [endpoint-component]]
            [duct.component.handler :refer [handler-component]]
            [duct.middleware.not-found :refer [wrap-not-found]]
            [meta-merge.core :refer [meta-merge]]
            [ring.component.jetty :refer [jetty-server]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.cors :refer [wrap-cors]]
            [catalogue.endpoint.files :refer [files-endpoint]]
            [catalogue.endpoint.example :refer [example-endpoint]]
            [catalogue.component.mongo :refer [new-mongo-db]]))

(def base-config
  {:app {:middleware [[wrap-not-found :not-found]
                      [wrap-defaults :defaults]
                      [wrap-cors :allow]]
         :not-found  "Resource Not Found"
         :defaults   (meta-merge api-defaults {})
         :allow      #".*"}}) ;; change this later!!!!

(defn new-system [config]
  (let [config (meta-merge base-config config)]
    (-> (component/system-map
         :app  (handler-component (:app config))
         :http (jetty-server (:http config))
         :db (new-mongo-db (-> config :db :uri))
         :example (endpoint-component example-endpoint)
         :files (endpoint-component files-endpoint))
        (component/system-using
         {:http [:app]
          :app  [:files :example]
          :files [:db]
          :example []}))))

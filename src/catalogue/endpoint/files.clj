(ns catalogue.endpoint.files
  (:require [clojure.java.io :as io]
            [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [monger.collection :as mc]
            [dire.core :refer [with-handler!]]))

(s/defschema File
 {:id s/Str
  :location {:latitude Double :longitude Double }})

(s/defschema Detail
  {:id s/Str
   :owner {:name s/Str
           :contact {:email s/Str
                     :telephone s/Str}}})

(s/defschema Catalogue
 {:detail Detail
  :files [File]})

(defn db-find [db coll query]
  (->> (mc/find-maps db coll query)
       (map #(dissoc % :_id))))

(defn db-find-one [db coll query]
  (-> (mc/find-one-as-map db coll query)
      (dissoc :_id)))

(defn get-files [db]
  (db-find db "files" {}))

(with-handler! #'db-find
  java.lang.Exception
  (fn [e & args] (println e)))

(with-handler! #'db-find-one
  java.lang.Exception
  (fn [e & args] (println e)))

(defn get-catalogue-detail [db]
  (db-find-one db "detail" {}))

(defn get-catalogue [db]
  (let [files (get-files db)
        catalogue-detail (get-catalogue-detail db)]
    (assoc {}
      :detail catalogue-detail
      :files files)))

(defn files-endpoint [{db :db}]
 (api
  {:swagger
   {:ui "/api-docs"
    :spec "swagger.json"
    :data {:info {:title "Catalogue API"
                    :description "Spatial Data Files"}
             :tags [{:name "api"}]}}}
  (context "/api" []
           :tags ["files"]

           (GET "/catalogue" []
                :return  Catalogue
                :summary "returns the entire catalogue"
                (ok (get-catalogue (:db db))))

           (GET "/catalogue/detail" []
                :return  Detail
                :summary "returns the entire catalogue"
                (ok (get-catalogue-detail (:db db))))
           
           (GET "/catalogue/files" []
                :return  [File]
                :summary "returns all files"
                (ok (get-files (:db db)))))))

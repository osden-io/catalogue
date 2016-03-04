(ns catalogue.endpoint.files
  (:require [clojure.java.io :as io]
            [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [monger.collection :as mc]))

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

;; TODO exceptions etc
(defn get-files [db]
  (let [files (map #(dissoc % :_id) (mc/find-maps db "files" {}))]
    files))

(defn get-catalogue-detail [db]
  (let [detail (dissoc (mc/find-one-as-map db "detail" {}) :_id)]
     detail))

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

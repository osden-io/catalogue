(ns catalogue.endpoint.files
  (:require [clojure.java.io :as io]
           [ring.util.http-response :refer :all]
           [compojure.api.sweet :refer :all]
           [schema.core :as s]
           [monger.collection :as mc]))

(s/defschema File
 {:id s/Str
  :location {:latitude Double :longitude Double }})

(s/defschema Catalogue
 {:id s/Str
  :owner-details {:name s/Str
                  :contact {:email s/Str
                            :telephone s/Str}}
  :files [File]})

(defn get-files [db]
   (mc/find-maps db "files" {}))

(defn get-catalogue [db]
  (let [files (get-files db)]
    (assoc {:id "land-solution"
            :owner-details {:name "Land Solution"
                            :contact {:email "lee.hellen@landsolution.com.au"
                                      :telephone "07332933982"}}}
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
           
           (GET "/catalogue/files" []
                :return  [File]
                :summary "returns all files"
                (ok (get-files (:db db)))))))

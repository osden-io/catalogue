(ns catalogue.endpoint.files
  (:require [clojure.java.io :as io]
           [ring.util.http-response :refer :all]
           [compojure.api.sweet :refer :all]
           [schema.core :as s]))

(s/defschema File
 {:id s/Str
  :location {:latitude Double :longitude Double }})

(s/defschema Catalogue
 {:id s/Str
  :owner-details {:name s/Str
                  :contact {:email s/Str
                            :telephone s/Str}}
  :files [File]})

(defn files []
  [
   {
    :id "xxx0"
    :location {:latitude -28.73 :longitude 153.467864}
    }
   {
    :id "xxx1"
    :location {:latitude -28.74 :longitude 153.467864}
    }
   {:id "xxx2"
    :location {:latitude -28.75 :longitude 153.467864}
    }
   {:id "xxx3"
    :location {:latitude -28.76 :longitude 153.467864}
    }
   ])


(defn files-endpoint [config]
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
                (println "catalogue request received")
                (let [contents (files)]
                  :return  [Catalogue]
                  :summary "returns all files"
                  (ok {
                       :id "land-solution"
                       :owner-details {:name "Land Solution"
                                       :contact {
                                                 :email "lee.hellen@landsolution.com.au"
                                                 :telephone "07332933982"
                                                 }}
                       :files contents})))
           
           (GET "/catalogue/files" []
                :return  [File]
                :summary "returns all files"
                (ok (files)  ))
           
           ;; (GET* "/user/:id"  []
           ;;       :return      User
           ;;       :path-params [id :- String]
           ;;       :summary     "returns the user with a given id"
           ;;       (ok (db/get-users {:id id} (:db config))))

           ;; (POST* "/authenticate" []
           ;;        :return         Boolean
           ;;        :body-params    [user :- User]
           ;;        :summary        "authenticates the user using the id and pass."
           ;;        (ok (db/authenticate user (:db config))))
           
           ;; (POST* "/user"      []
           ;;        :return      Long
           ;;        :body-params [user :- User]
           ;;        :summary     "creates a new user record."
           ;;        (ok (db/create-user-account! user (:db config))))
           
           ;; (DELETE* "/user"    []
           ;;        :return      Long
           ;;        :body-params [id :- String]
           ;;        :summary     "deletes the user record with the given id."
           ;;        (ok (db/delete-user! {:id id} (:db config))))
           )))

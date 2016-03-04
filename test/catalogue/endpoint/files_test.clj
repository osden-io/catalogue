(ns catalogue.endpoint.files-test
  (:require [com.stuartsierra.component :as component]
            [clojure.test :refer :all]
            [kerodon.core :refer :all]
            [kerodon.test :refer :all]
            [catalogue.endpoint.files :as files]
            [catalogue.component.mongo :refer [new-mongo-db]]
            [monger.core :as mg]
            [monger.collection :as mc]
            [ring.mock.request :as mock]
            [cheshire.core :as cheshire]))

(def db-component (atom {}))

(def detail-stub {:id "land-solution"
                  :owner {:name "Land Solution"
                          :contact {:email "lee.hellen@landsolution.com.au"
                                    :telephone "07332933982"}}})

(def files-stub [{:id "1"
                   :location {:latitude -28.73
                              :longitude 153.467864}}
                  {:id "2"
                   :location {:latitude -28.73
                              :longitude 153.567864}}])

(defn insert-stub [db-comp coll data]
  (let [db (:db @db-comp)]
    (if (vector? data)
      (mc/insert-batch db coll data)
      (mc/insert-and-return db coll data))))

(defn remove-setup [db-comp]
  (mc/remove (:db @db-comp) "files" {})
  (mc/remove (:db @db-comp) "detail" {}))

(use-fixtures :once
  (fn [f]
    (reset! db-component (component/start (new-mongo-db)))
    (f)
    (component/stop @db-component)))

(use-fixtures :each
  (fn [f]
    (insert-stub db-component "detail" detail-stub)
    (insert-stub db-component "files" files-stub)
    (f)
    (remove-setup db-component)))

(defn handler [db]
  (files/files-endpoint {:db db}))

(defn parse-body [body]
  (cheshire/parse-string (slurp body) true))

(deftest catalogue-api
  (testing "catalogue resource exists"
    (-> (session (handler @db-component))
        (visit "/api/catalogue")
        (has (status? 200) "catalogue resource does not exist")))

  (testing "files resource exists"
    (-> (session (handler @db-component))
        (visit "/api/catalogue/files")
        (has (status? 200) "files resource does not exist"))))

(deftest catalogue-test
  (testing "GET catalogue resource returns entire catalogue"
    (let [response ((handler @db-component) (mock/request :get "/api/catalogue"))
          body (parse-body (:body response))]
      (is (= (response :status) 200))
      (is (= (get-in response [:headers "Content-Type"])
             "application/json; charset=utf-8"))
      (is (= {:detail detail-stub :files files-stub} body))))

  (testing "GET catalogue detail resource returns correct information"
    (let [response ((handler @db-component) (mock/request :get "/api/catalogue/detail"))
          body (parse-body (:body response))]
      (is (= (response :status) 200))
      (is (= (get-in response [:headers "Content-Type"])
             "application/json; charset=utf-8"))
      (is (= detail-stub body)))))

(deftest db-methods
  (testing "db-find does not return mongo _id"
    (let [result (files/db-find @db-component "files" {})]
      (is (not-any? #(= :_id %) (mapcat keys result)))))
  
  (testing "db-find does not throw null pointer exceptions"
    (files/db-find nil nil nil))

  (testing "db-find-one does not return mongo _id"
    (let [result (files/db-find @db-component "files" {})]
      (is (not-any? #(= :_id %) (keys result)))))  
  
  (testing "db-find-one does not throw null pointer exceptions"
    (files/db-find-one nil nil nil)))

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

(def db (atom {}))

(defn insert-detail-stub [db]
  (mc/insert-and-return (:db @db) "detail"
             {:id "land-solution"
              :owner {:name "Land Solution"
                      :contact {:email "lee.hellen@landsolution.com.au"
                                :telephone "07332933982"}}}))

(defn remove-setup [db]
  (mc/remove (:db @db) "detail" {}))

(use-fixtures :once
  (fn [f]
    (reset! db (component/start (new-mongo-db)))
    (f)
    (component/stop @db)))

(use-fixtures :each
  (fn [f]
    (insert-detail-stub db)
    (f)
    (remove-setup db)))

(defn handler [db]
  (files/files-endpoint {:db db}))

(defn parse-body [body]
  (cheshire/parse-string (slurp body) true))

(deftest catalogue-api
  (testing "catalogue resource exists"
    (-> (session (handler @db))
        (visit "/api/catalogue")
        (has (status? 200) "catalogue resource does not exist")))

  (testing "files resource exists"
    (-> (session (handler @db))
        (visit "/api/catalogue/files")
        (has (status? 200) "files resource does not exist"))))

(deftest catalogue-test
  (testing "GET catalogue resource returns entire catalogue"
    (let [response ((handler @db) (mock/request :get "/api/catalogue"))
          body (parse-body (:body response))]
      (is (= (response :status) 200))
      (is (= (get-in response [:headers "Content-Type"])
             "application/json; charset=utf-8"))
      (is (= [:detail :files] (keys body)))))

  (testing "GET catalogue detail resource returns correct information"
    (let [response ((handler @db) (mock/request :get "/api/catalogue/detail"))
          body (parse-body (:body response))]
      (is (= (response :status) 200))
      (is (= (get-in response [:headers "Content-Type"])
             "application/json; charset=utf-8"))
      (is (= [:id :owner] (keys body)))))

  (testing "db-find does not throw null pointer exceptions"
    (files/db-find nil nil nil))

  (testing "db-find-one does not throw null pointer exceptions"
    (files/db-find-one nil nil nil))))


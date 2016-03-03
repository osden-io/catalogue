(ns catalogue.endpoint.files-test
  (:require [com.stuartsierra.component :as component]
            [clojure.test :refer :all]
            [kerodon.core :refer :all]
            [kerodon.test :refer :all]
            [catalogue.endpoint.files :as files]
            [catalogue.component.mongo :refer [new-mongo-db]]
            [monger.core :as mg]
            [ring.mock.request :as mock]
            [cheshire.core :as cheshire]))

(def db (atom {}))

(use-fixtures :once
  (fn [f]
    (reset! db (component/start (new-mongo-db)))
    (f)))

(use-fixtures :each
  (fn [f]
    (f)))

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
  (testing "catalogue resource returns correct information"
    (let [response ((handler @db) (mock/request :get "/api/catalogue"))
          body (parse-body (:body response))]
      (is (= (response :status) 200))
      (is (= (get-in response [:headers "Content-Type"])
             "application/json; charset=utf-8"))
      (is (= [:id :owner-details :files] (keys body))))))

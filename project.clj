(defproject catalogue "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.stuartsierra/component "0.3.0"]
                 [compojure "1.4.0"]
                 [duct "0.5.8"]
                 [environ "1.0.2"]
                 [meta-merge "0.1.1"]
                 [ring "1.4.0"]
                 [ring/ring-defaults "0.1.5"]
                 [jumblerg/ring.middleware.cors "1.0.1"]
                 [ring-jetty-component "0.3.0"]
                 [metosin/compojure-api "1.0.0"]
                 [prismatic/schema "1.0.5"]
                 [com.novemberain/monger "3.0.2"]
                 [cheshire "5.5.0"]
                 [dire "0.5.4"]]
  :plugins [[lein-environ "1.0.2"]
            [lein-gen "0.2.2"]]
  :generators [[duct/generators "0.5.8"]]
  :duct {:ns-prefix catalogue}
  :main ^:skip-aot catalogue.main
  :target-path "buildoutput/%s/"
  :aliases {"gen"   ["generate"]
            "setup" ["do" ["generate" "locals"]]}
  :profiles
  {:dev  [:project/dev  :profiles/dev]
   :test [:project/test :profiles/test]
   :uberjar {:aot :all}
   :profiles/dev  {}
   :profiles/test {}
   :project/dev   {:dependencies [[reloaded.repl "0.2.1"]
                                  [org.clojure/tools.namespace "0.2.11"]
                                  [org.clojure/tools.nrepl "0.2.12"]
                                  [eftest "0.1.0"]
                                  [kerodon "0.7.0"]
                                  [ring/ring-mock "0.3.0"]]
                   :source-paths ["dev"]
                   :repl-options {:init-ns user}
                   }
   :project/test  {}})

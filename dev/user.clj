(ns user
  (:require [clojure.tools.namespace.repl :as repl]
            [clojure.test :as test]))

(defn hello []
  (println "world"))

(defn run-project-tests []
  (test/run-all-tests #"vineyard\..*"))

(defn refresh []
  (repl/refresh :after 'user/run-project-tests))

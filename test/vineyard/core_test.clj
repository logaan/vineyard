(ns vineyard.core-test
  (:require [clojure.test :refer :all]
            [vineyard.core :as vy]
            [clojure.java.io :as io]
            [clojure.java.shell :as shell]))

(def hello-world-sexp
  (vy/->Call "log" [(vy/->Text "Hello, World.")]))

(deftest hello-world-compiles
  (is (= "log(\"Hello, World.\")"
         (vy/compile hello-world-sexp))))

(deftest hello-world-saves
  (let [path "target/out.js"]
    (vy/save (vy/compile hello-world-sexp) path)
    (is (= {:exit 0 :out "Hello, World.\n" :err ""}
           (shell/sh "node" path)))))

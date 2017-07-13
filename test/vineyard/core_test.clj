(ns vineyard.core-test
  (:require [clojure.test :refer :all]
            [vineyard.core :as vy]
            [clojure.java.io :as io]
            [clojure.java.shell :as shell]))

(defn run [sexp]
  (let [path "target/out.js"]
    (vy/save (vy/compile sexp) path)
    (shell/sh "node" path)))

(def hello-world-sexp
  (vy/->Call "log" [(vy/->Text "Hello, World.")]))

(deftest hello-world-compiles
  (is (= "log(\"Hello, World.\")"
         (vy/compile hello-world-sexp))))

(deftest hello-world-saves
  (is (= {:exit 0
          :out "Hello, World.\n"
          :err ""}
         (run hello-world-sexp))))

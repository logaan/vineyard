(ns vineyard.hello-world-test
  (:require [vineyard.core :as core]
            [vineyard.compiler :as compiler]
            [vineyard.parser :as parser]
            [clojure.test :as t :refer [deftest is]]
            [clojure.java.io :as io]
            [clojure.string :as string]))

(defn source []
  (slurp (io/resource "01_hello_world.vy")))

(defn sexp []
  (read-string (slurp (io/resource "01_hello_world.clj"))))

(defn js []
  (slurp (io/resource "01_hello_world.js")))

(defn out []
  (slurp (io/resource "01_hello_world.out")))

(defn error-free [out]
  {:exit 0
   :out out
   :err ""})

(deftest runs-end-to-end
  (let [parsed   (parser/parse (source))
        compiled (compiler/compile parsed)
        ran      (core/run compiled)]
    (is (= (sexp) parsed))
    (is (= (string/trim (js)) compiled))
    (is (= (error-free (out)) ran))))

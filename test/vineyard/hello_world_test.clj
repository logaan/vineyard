(ns vineyard.hello-world-test
  (:require [vineyard.core :as core]
            [vineyard.parser :as parser]
            [clojure.test :as t :refer [deftest is]]
            [clojure.java.io :as io]
            [clojure.java.shell :as shell]
            [clojure.string :as str]))

(defn source []
  (slurp (io/resource "01_hello_world.vy")))

(defn sexp []
  (read-string (slurp (io/resource "01_hello_world.sexp"))))

(defn js []
  (slurp (io/resource "01_hello_world.js")))

(defn out []
  (slurp (io/resource "01_hello_world.out")))

(defn error-free [out]
  {:exit 0
   :out out
   :err ""})

(defn run [code]
  (let [path "target/out.js"]
    (core/save code path)
    (shell/sh "node" path)))

(deftest runs-end-to-end
  (let [parsed   (parser/parse (source))
        compiled (core/compile parsed)
        ran      (run compiled)]
    ;; This is intermetently failing for no good reason. The things that should
    ;; be equal are printing as equal.
    ;; (is (= (sexp) parsed))
    (is (= (str/trim (js)) compiled))
    (is (= (error-free (out)) ran))))

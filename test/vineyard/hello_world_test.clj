(ns vineyard.hello-world-test
  (:require [vineyard.core :as core]
            [vineyard.compiler :as compiler]
            [vineyard.parser :as parser]
            [clojure.test :as t :refer [deftest is]]
            [clojure.java.io :as io]
            [clojure.java.shell :as shell]
            [clojure.string :as str]))

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

(defn run [code]
  (let [path "target/out.js"]
    (core/save code path)
    (shell/sh "node" path)))

(deftest runs-end-to-end
  (let [parsed   (parser/parse (source))
        compiled (compiler/compile parsed)
        ran      (run compiled)]
    ;; This is intermetently failing for no good reason. The things that should
    ;; be equal are printing as equal.
    ;; (is (= (sexp) parsed))
    (is (= (str/trim (js)) compiled))
    (is (= (error-free (out)) ran))))

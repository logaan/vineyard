(ns vineyard.block-test
  (:require [vineyard.block :as sut]
            [clojure.test :refer [deftest is]]
            [vineyard.data :as data]
            [clojure.java.io :as io]
            [vineyard.compiler :as compiler]
            [vineyard.core :as core]
            [vineyard.parser :as parser]
            [clojure.string :as string]
            [vineyard.test-helpers :as test-helpers]))

(deftest blocking-calls
  (is (sut/blocking-call? (data/call "sleep" [])))
  (is (not (sut/blocking-call? (data/call "log" []))))
  (is (not (sut/blocking-call? (data/text "kittens")))))

(def hi-sleep-bye-pre
  [(data/call "log" [(data/text "hi")])
   (data/call "sleep" [])
   (data/call "log" [(data/text "bye")])])

(deftest finds-blocking
  (is (= [[(data/call "log" [(data/text "hi")])]
          (data/call "sleep" [])
          [(data/call "log" [(data/text "bye")])]]
         (sut/find-blocking hi-sleep-bye-pre))))

(defn read-test-data [directory]
  (letfn [(slurp-res [filename] (slurp (io/resource (str directory filename))))]
    {:source       (slurp-res "1_source.vy")
     :parse-sexp   (read-string (slurp-res "2_parse_sexp.clj"))
     :compile-sexp (read-string (slurp-res "3_compile_sexp.clj"))
     :compiled     (string/trim (slurp-res "4_compiled.js"))
     :output       (slurp-res "5_output.txt")}))

(defn run-from-test-data [directory]
  (let [expected     (read-test-data directory)
        parse-sexp   (parser/parse (:source expected))
        compile-sexp (sut/make-blocking parse-sexp)
        compiled     (compiler/compile compile-sexp)
        output       (core/run compiled)]
    (is (= (:parse-sexp expected) parse-sexp))
    (is (= (:compile-sexp expected) compile-sexp))
    (is (= (:compiled expected) compiled))
    (is (= (test-helpers/error-free (:output expected)) output))))

(deftest runs-multiple
  (run-from-test-data "01_hello_world/")
  (run-from-test-data "02_hi_sleep_bye/")
  (run-from-test-data "03_hi_sleep_ok_sleep_bye/"))

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

(defn hi-sleep-bye-pre []
  (read-string (slurp (io/resource "02_hi_sleep_bye.pre.clj"))))

(deftest finds-blocking
  (is (= [[(data/call "log" [(data/text "hi")])]
          (data/call "sleep" [])
          [(data/call "log" [(data/text "bye")])]]
         (sut/find-blocking (hi-sleep-bye-pre)))))

(defn hi-sleep-bye-post []
  (read-string (slurp (io/resource "02_hi_sleep_bye.post.clj"))))

(deftest passes-continuation-to-first-blocking
  (is (= (hi-sleep-bye-post)
         (sut/pass-continuation-to-first-blocking (hi-sleep-bye-pre)))))

(defn hello-world []
  (read-string (slurp (io/resource "01_hello_world.clj"))))

(deftest leaves-non-blocking-alone []
  (is (= (hello-world)
         (sut/pass-continuation-to-first-blocking (hello-world)))))

(defn source []
  (slurp (io/resource "03_hi_sleep_ok_sleep_bye.vy")))

(defn pre []
  (read-string (slurp (io/resource "03_hi_sleep_ok_sleep_bye.pre.clj"))))

(defn post []
  (read-string (slurp (io/resource "03_hi_sleep_ok_sleep_bye.post.clj"))))

(defn js []
  (slurp (io/resource "03_hi_sleep_ok_sleep_bye.js")))

(defn out []
  (slurp (io/resource "03_hi_sleep_ok_sleep_bye.out")))

(deftest can-handle-multiple-top-level-blocking-calls []
  (is (= (post)
         (sut/pass-continuation-to-first-blocking (pre)))))

(deftest runs-multiple
  (let [parsed   (parser/parse (source))
        blocking (sut/pass-continuation-to-first-blocking parsed)
        compiled (compiler/compile blocking)
        ran      (core/run compiled)]
    (is (= (pre) parsed))
    (is (= (post) blocking))
    (is (= (string/trim (js)) compiled))
    (is (= (test-helpers/error-free (out)) ran))))

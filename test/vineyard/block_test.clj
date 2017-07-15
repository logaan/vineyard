(ns vineyard.block-test
  (:require [vineyard.block :as sut]
            [clojure.test :refer [deftest is]]
            [vineyard.data :as data]
            [clojure.java.io :as io]))

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

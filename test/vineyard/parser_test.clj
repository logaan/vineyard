(ns vineyard.parser-test
  (:require [vineyard.data :refer [call text]]
            [vineyard.parser :as sut]
            [clojure.test :refer [deftest is]]))

(deftest parses-hello-world
  (is (= [(call "log" [(text "Hello, World.")])]
         (sut/parse "(log \"Hello, World.\")"))))

(ns vineyard.parser-text
  (:require [vineyard.core :as vy]
            [vineyard.parser :as sut]
            [clojure.test :refer [deftest is]]))

(deftest parses-hello-world
  (is (= [(vy/->Call "log" [(vy/->Text "Hello, World.")])]
         (sut/parse "(log \"Hello, World.\")"))))

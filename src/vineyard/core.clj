(ns vineyard.core
  (:require [clojure.string :as string]
            [clojure.java.io :as io]))

(defn save [program path]
  (let [before (slurp (io/resource "before.js"))
        after  (slurp (io/resource "after.js"))]
    (spit path (str before program after))))

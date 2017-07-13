(ns vineyard.core
  (:require [clojure.string :as string]
            [clojure.java.io :as io])
  (:refer-clojure :exclude [compile]))

(defprotocol Compile
  (compile [this]))

(defrecord Text [text]
  Compile
  (compile [_]
    (str \" text \")))

(defrecord Call [fn-name arguments]
  Compile
  (compile [_]
    (println arguments)
    (let [arg-string (->> (map compile arguments)
                          (string/join ","))]
      (str fn-name "(" arg-string ")"))))

(defn save [program path]
  (let [before (slurp (io/resource "before.js"))
        after  (slurp (io/resource "after.js"))]
    (spit path (str before program after))))

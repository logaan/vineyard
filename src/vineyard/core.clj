(ns vineyard.core
  (:require [clojure.string :as string]
            [clojure.java.io :as io]
            [clojure.string :as str])
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
    (let [arg-string (->> (map compile arguments)
                          (string/join ", "))]
      (str fn-name "(" arg-string ")"))))

(extend-type clojure.lang.PersistentVector
  Compile
  (compile [coll]
    (str/join "\n" (map compile coll))))

(defn save [program path]
  (let [before (slurp (io/resource "before.js"))
        after  (slurp (io/resource "after.js"))]
    (spit path (str before program after))))

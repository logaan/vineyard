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
    (let [arg-string (->> (map compile arguments)
                          (string/join ", "))]
      (str fn-name "(" arg-string ")"))))

;; parameters (seq String)
;; body (vec Compileable)
(defrecord AnonymousFunction [parameters body]
  Compile
  (compile [_]
    (let [param-string (string/join ", " parameters)]
      (str "function(" param-string "){\n"
           (compile body)
           "\n}"))))

(extend-type clojure.lang.PersistentVector
  Compile
  (compile [coll]
    (str (string/join ";\n" (map compile coll)) ";")))

(defn save [program path]
  (let [before (slurp (io/resource "before.js"))
        after  (slurp (io/resource "after.js"))]
    (spit path (str before program after))))

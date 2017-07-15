(ns vineyard.compiler
  (:require [clojure.string :as string])
  (:refer-clojure :exclude [compile])
  (:import [vineyard.data Text Call AnonymousFunction]
           [clojure.lang PersistentVector]))

(defprotocol Compile
  (compile [this]))

(extend-type Text
  Compile
  (compile [{:keys [text]}]
    (str \" text \")))

(extend-type Call
  Compile
  (compile [{:keys [fn-name arguments]}]
    (let [arg-string (->> (map compile arguments)
                          (string/join ", "))]
      (str fn-name "(" arg-string ")"))))

(extend-type AnonymousFunction
  Compile
  (compile [{:keys [parameters body]}]
    (let [param-string (string/join ", " parameters)]
      (str "function(" param-string "){\n"
           (compile body)
           "\n}"))))

(extend-type PersistentVector
  Compile
  (compile [coll]
    (str (string/join ";\n" (map compile coll)) ";")))


(extend-type clojure.lang.PersistentVector$ChunkedSeq
  Compile
  (compile [coll]
    (str (string/join ";\n" (map compile coll)) ";")))

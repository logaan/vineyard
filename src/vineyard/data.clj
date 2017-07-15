(ns vineyard.data)

(defrecord Text [text])

(defn text [text]
  (Text. text))

(defrecord Call [fn-name arguments])

(defn call [fn-name arguments]
  (Call. fn-name arguments))

(defrecord AnonymousFunction [parameters body])

(defn anonymous-function [parameters body]
  (AnonymousFunction. parameters body))

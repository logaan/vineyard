(ns vineyard.block
  (:require [vineyard.data :as data])
  (:import [vineyard.data Text Call AnonymousFunction]
           [clojure.lang PersistentVector]))

(def blocking-fn?
  #{"sleep"})

(defn blocking-call? [{:keys [fn-name] :as expr}]
  (and (instance? Call expr)
       (blocking-fn? fn-name)))

(defn find-blocking [exprs]
  (let [[before [blocking & after]] (split-with (complement blocking-call?)
                                                exprs)]
    [(vec before) blocking after]))

(defn prepend-argument [call argument]
  (update-in call [:arguments] (fn [arguments] (into [argument] arguments))))

(defn pass-continuation-to-first-blocking [exprs]
  (let [[before call after] (find-blocking exprs)
        continuation        (data/anonymous-function [] after)
        new-blocking-call   (prepend-argument call continuation)]
    (conj before new-blocking-call)))

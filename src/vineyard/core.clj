(ns vineyard.core
  (:require [clojure.string :as string]
            [clojure.java.io :as io]
            [clojure.java.shell :as shell]))

(defn save [program path]
  (let [before (slurp (io/resource "before.js"))
        after  (slurp (io/resource "after.js"))]
    (spit path (str before program after))))

(defn run [code]
  (let [path "target/out.js"]
    (save code path)
    (shell/sh "node" path)))

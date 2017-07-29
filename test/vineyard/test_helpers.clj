(ns vineyard.test-helpers)

(defn error-free [out]
  {:exit 0
   :out out
   :err ""})

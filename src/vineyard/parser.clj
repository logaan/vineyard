(ns vineyard.parser
  (:require [vineyard.data :as data]
            [blancas.kern.core :refer :all :exclude [parse]]
            [blancas.kern.lexer.basic :refer :all]))

(declare expr)

(def text
  (bind [t string-lit]
        (return (data/text t))))

(def call
  (parens
   (bind [fn-name   identifier
          arguments (comma-sep expr)]
         (return (data/call fn-name arguments)))))

(def expr
  (<|> text call))

(def vineyard
  (many expr))

(defn parse [string]
  (value vineyard string))

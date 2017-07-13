(ns vineyard.parser
  (:require [vineyard.core :as core]
            [blancas.kern.core :refer :all :exclude [parse]]
            [blancas.kern.lexer.basic :refer :all]))

(declare expr)

(def text
  (bind [t string-lit]
        (return (core/->Text t))))

(def call
  (parens
   (bind [fn-name   identifier
          arguments (comma-sep expr)]
         (return (core/->Call fn-name arguments)))))

(def expr
  (<|> text call))

(def vineyard
  (many expr))

(defn parse [string]
  (value vineyard string))

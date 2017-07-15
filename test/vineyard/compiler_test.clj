(ns vineyard.compiler-test
  (:require [clojure.test :refer [deftest is]]
            [vineyard.data :refer [call text anonymous-function]]
            [vineyard.compiler :as sut]))

(def hello-world-sexp
  [(call "log" [(text "Hello, World.")])])

(deftest hello-world-compiles
  (is (= "log(\"Hello, World.\");"
         (sut/compile hello-world-sexp))))

(deftest multi-call-compiles
  (let [expr [(call "a" [(text "b")])
              (call "c" [(text "d")])]]
    (is (= "a(\"b\");\nc(\"d\");"
          (sut/compile expr)))))

(deftest nested-call-compiles
  (let [expr [(call "a" [(call "b" [(text "c")])])]]
    (is (= "a(b(\"c\"));"
           (sut/compile expr)))))

(deftest args-seperated-by-commas
  (let [expr [(call "a" [(text "b")(text "c")])]]
    (is (= "a(\"b\", \"c\");"
           (sut/compile expr)))))

(deftest annonymous-function
  (let [expr [(anonymous-function ["foo" "bar"] [(text "baz")])]]
    (is (= "function(foo, bar){\n\"baz\";\n};"
           (sut/compile expr)))))

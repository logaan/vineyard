(ns vineyard.core-test
  (:require [clojure.test :refer :all]
            [vineyard.core :as vy]))

(def hello-world-sexp
  [(vy/->Call "log" [(vy/->Text "Hello, World.")])])

(deftest hello-world-compiles
  (is (= "log(\"Hello, World.\");"
         (vy/compile hello-world-sexp))))

(deftest multi-call-compiles
  (let [expr [(vy/->Call "a" [(vy/->Text "b")])
              (vy/->Call "c" [(vy/->Text "d")])]]
    (is (= "a(\"b\");\nc(\"d\");"
          (vy/compile expr)))))

(deftest nested-call-compiles
  (let [expr [(vy/->Call "a" [(vy/->Call "b" [(vy/->Text "c")])])]]
    (is (= "a(b(\"c\"));"
           (vy/compile expr)))))

(deftest args-seperated-by-commas
  (let [expr [(vy/->Call "a" [(vy/->Text "b")(vy/->Text "c")])]]
    (is (= "a(\"b\", \"c\");"
           (vy/compile expr)))))

(deftest annonymous-function
  (let [expr [(vy/->AnonymousFunction ["foo" "bar"] [(vy/->Text "baz")])]]
    (is (= "function(foo, bar){\n\"baz\";\n};"
           (vy/compile expr)))))

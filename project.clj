(defproject vineyard "0.1.0-SNAPSHOT"
  :description "A small lisp with blocking concurrency semantics and threads
                that compiles to Javascript."
  :url "https://github.com/logaan/vineyard"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.blancas/kern "1.1.0"]]
  :profiles {:dev {:resource-paths ["dev"]
                   :dependencies [[org.clojure/tools.namespace "0.2.11"]]}})

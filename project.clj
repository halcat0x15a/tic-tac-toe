(defproject tic-tac-toe "0.1.0-SNAPSHOT"
  :description "tic tac toe"
  :url "http://halcat0x15a.github.com/tic-tac-toe/"
  :plugins [[lein-cljsbuild "0.2.9"]]
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/core.logic "0.7.5"]
                 [org.clojure/tools.nrepl "0.2.0-beta10"]
                 [com.oracle/javafx-runtime "2.2"]]
  :cljsbuild {:builds [{:source-path "src-cljs"}]
              :crossovers [tic-tac-toe.core
                           tic-tac-toe.game]
              :crossover-path "src-cljs"
              :crossover-jar true}
  :main tic-tac-toe.main)

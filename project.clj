(defproject gist "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :plugins [[lein-cljsbuild "0.2.9"]]
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/core.logic "0.7.5"]
                 [org.clojure/tools.nrepl "0.2.0-beta10"]]
  :resource-paths ["/usr/lib/jvm/javafx-sdk/rt/lib/jfxrt.jar"]
  :cljsbuild {:builds [{:source-path "src-cljs"}]
              :crossovers [tic-tac-toe.core]
              :crossover-path "src-cljs"
              :crossover-jar false}
  :main tic-tac-toe.main)

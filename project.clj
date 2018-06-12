(defproject clj-input-text "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [halgari/fn-fx "0.4.0"]]
  :main clj-input-text.core
  :aot [clj-input-text.core]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

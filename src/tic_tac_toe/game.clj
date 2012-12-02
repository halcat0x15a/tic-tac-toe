(ns tic-tac-toe.game
  (:require [tic-tac-toe.core :as core])
  ;*CLJSBUILD-REMOVE*;(:use-macros [cljs.core.logic.macros :only [run*]])
  )

;*CLJSBUILD-REMOVE*;(comment
(use '[clojure.core.logic :only [run*]])
;*CLJSBUILD-REMOVE*;)

(def number 3)

(def window-size (* number 100))

(def panel-size (/ window-size number))

(def font-size (/ window-size 5))

(def radius (/ panel-size 2))

(defrecord Panel [x y sym])

(defprotocol Canvas
  (draw-circle [this panel])
  (draw-cross [this panel])
  (draw-text [this text]))

(defprotocol Symbol
  (draw [this canvas panel]))

(def o
  (reify Symbol
    (draw [this canvas panel]
      (draw-circle canvas panel))))

(def x
  (reify Symbol
    (draw [this canvas panel]
      (draw-cross canvas panel))))

(def finish? (atom false))

(def panels
  (for [x (range number) y (range number)]
    (Panel. (* x panel-size)
            (* y panel-size)
            (atom nil))))

(defn syms []
  (map (comp deref :sym) panels))

(defn finish [canvas s]
  (draw-text canvas s)
  (reset! finish? true))

(defn write [canvas panel sym]
  (when-not @finish?
    (reset! (:sym panel) sym)
    (draw sym canvas panel)
    (if-let [result (first (run* [q] (apply core/end q o (syms))))]
      (if (= result o)
        (finish canvas "Win")
        (finish canvas "Lose"))
      (when (every? (comp not nil?) (syms))
        (finish canvas "Draw")))))

(defn play [canvas panel]
  (when-not @(:sym panel)
    (doto canvas
      (write panel o)
      (write (-> (run* [q] (apply core/play q x (syms)))
                 first
                 (zipmap panels)
                 (get x))
             x))))

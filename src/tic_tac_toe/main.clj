(ns tic-tac-toe.main
  (:refer-clojure :exclude [==])
  (:use [clojure.core.logic])
  (:require [tic-tac-toe.core :as core])
  (:gen-class
   :extends javafx.application.Application)
  (:import
   [javafx.application Application]
   [javafx.scene Scene Group]
   [javafx.event EventHandler]
   [javafx.scene.canvas Canvas]
   [javafx.scene.shape Rectangle Circle Line]
   [javafx.scene.text Text Font]
   [javafx.scene.paint Color]))

(def number 3)

(def window-size (* number 100))

(def panel-size (/ window-size number))

(def font-size (/ window-size 5))

(def radius (/ panel-size 2))

(def font (Font. font-size))

(def finish? (atom false))

(defprotocol Symbol
  (draw [this rect node]))

(def o
  (reify Symbol
    (draw [this rect node]
      (.add node
            (Circle. (+ (.getX rect) radius)
                     (+ (.getY rect) radius)
                     radius)))))

(def x
  (reify Symbol
    (draw [this rect node]
      (let [x (.getX rect)
            y (.getY rect)]
        (doto node
          (.add (Line. x y (+ x panel-size) (+ y panel-size)))
          (.add (Line. (+ x panel-size) y x (+ y panel-size))))))))

(defrecord Panel [rect sym])

(def panels
  (for [x (range number) y (range number)]
    (Panel. (doto (Rectangle. (* x panel-size)
                              (* y panel-size)
                              panel-size
                              panel-size)
              (.setFill Color/WHITE)
              (.setStroke Color/BLACK))
            (atom nil))))

(defn syms []
  (map (comp deref :sym) panels))

(defn finish [s node]
  (.add node
        (doto (Text. 0 font-size s)
          (.setFont font)
          (.setFill Color/GRAY)))
    (reset! finish? true))

(defn play [panel sym node]
  (when-not @finish?
    (reset! (:sym panel) sym)
    (draw sym (:rect panel) node)
    (if-let [result (first (run* [q] (apply core/end q o (syms))))]
      (if (= result o)
        (finish "Win" node)
        (finish "Lose" node))
      (when (every? (comp not nil?) (syms))
        (finish "Draw" node)))))

(defn write [panel node]
  (reify EventHandler
    (handle [this event]
      (when-not @(:sym panel)
        (play panel o node)
        (play (-> (run* [q] (apply core/play q x (syms)))
                  first
                  (zipmap panels)
                  (get x))
              x
              node)))))

(defn -start [this stage]
  (let [root (Group.)
        scene (Scene. root)
        children (.getChildren root)]
    (doseq [panel panels]
      (.add children
            (doto (:rect panel)
              (.setOnMouseClicked (write panel children)))))
    (doto stage
      (.setTitle "Tic Tac Toe")
      (.setScene scene)
      (.setMinHeight window-size)
      (.setMinWidth window-size)
      (.setResizable false)
      (.show))))

(defn -main [& args]
  (Application/launch tic_tac_toe.main args))

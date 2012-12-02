(ns tic-tac-toe.main
  (:refer-clojure :exclude [==])
  (:use [clojure.core.logic])
  (:require [tic-tac-toe.game :as game])
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

(def font (Font. game/font-size))

(defn -start [this stage]
  (let [root (Group.)
        scene (Scene. root)
        children (.getChildren root)
        canvas (reify game/Canvas
                 (draw-circle [this panel]
                   (.add children
                         (Circle. (+ (:x panel) game/radius)
                                  (+ (:y panel) game/radius)
                                  game/radius)))
                 (draw-cross [this panel]
                   (let [{:keys [x y]} panel
                         x' (+ x game/panel-size)
                         y' (+ y game/panel-size)]
                     (doto children
                       (.add (Line. x y
                                    x' y'))
                       (.add (Line. x' y
                                    x y')))))
                 (draw-text [this s]
                   (.add children
                         (doto (Text. 0 game/font-size s)
                           (.setFont font)
                           (.setFill Color/GRAY)))))]
    (doseq [panel game/panels]
      (.add children
            (doto (Rectangle. (:x panel)
                              (:y panel)
                              game/panel-size
                              game/panel-size)
              (.setFill Color/WHITE)
              (.setStroke Color/BLACK)
              (.setOnMouseClicked (reify EventHandler
                                    (handle [this event]
                                      (game/play canvas panel)))))))
    (doto stage
      (.setTitle "Tic Tac Toe")
      (.setScene scene)
      (.setMinHeight game/window-size)
      (.setMinWidth game/window-size)
      (.setResizable false)
      (.show))))

(defn -main [& args]
  (Application/launch tic_tac_toe.main args))

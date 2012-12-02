(ns tic-tac-toe.main
  (:require [cljs.core.logic :as logic]
            [tic-tac-toe.game :as game]
            [goog.dom :as dom]
            [goog.graphics :as graphics]
            [goog.events.EventType :as event-type]))

(def g (graphics/createGraphics 300 300))

(def white-fill (graphics/SolidFill. "white"))

(def gray-fill (graphics/SolidFill. "gray"))

(def black-fill (graphics/SolidFill. "black"))

(def black-stroke (graphics/Stroke. 1 "black"))

(def font (graphics/Font. game/font-size "monospace"))

(def canvas
  (reify game/Canvas
    (draw-circle [this panel]
      (.drawCircle g
                   (+ (:x panel) game/radius)
                   (+ (:y panel) game/radius)
                   game/radius
                   nil
                   black-fill))
    (draw-cross [this panel]
      (let [{:keys [x y]} panel
            x' (+ x game/panel-size)
            y' (+ y game/panel-size)]
        (doto g
          (.drawPath (doto (graphics/Path.)
                       (.moveTo x y)
                       (.lineTo x' y'))
                     black-stroke
                     nil)
          (.drawPath (doto (graphics/Path.)
                       (.moveTo x' y)
                       (.lineTo x y'))
                     black-stroke
                     nil))))
    (draw-text [this s]
      (.drawText g s 0 0 game/panel-size game/font-size "left" "top" font nil gray-fill))))

(defn run []
  (doseq [panel game/panels]
    (doto (.drawRect g (:x panel) (:y panel) game/panel-size game/panel-size black-stroke white-fill)
      (.addEventListener event-type/CLICK
                         (fn [event]
                           (game/play canvas panel)))))
  (.render g (dom/getElement "canvas")))

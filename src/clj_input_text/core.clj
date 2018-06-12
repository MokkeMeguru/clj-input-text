(ns clj-input-text.core
  (:require [fn-fx.fx-dom :as dom]
            [fn-fx.diff :refer [component defui render should-update?]]
            [fn-fx.controls :as ui])
  (:gen-class))


(defui Translater
  (render [this {:keys [res tres cel far start-time]}]
          (ui/grid-pane
           :alignment :center
           :hgap 10
           :vgap 10
           :padding (ui/insets
                     :bottom 25
                     :left 25
                     :right 25
                     :top 25)
           :children [(ui/button :text "Timer Start!"
                                 :on-action
                                 {:event :start-timer}
                                 :grid-pane/column-index 0
                                 :grid-pane/row-index 0
                                 :grid-pane/column-span 2
                                 :grid-pane/row-span 1)
                      (ui/label :text "Enter=>Translate"
                                :grid-pane/column-index 0
                                :grid-pane/row-index 1)
                      (ui/h-box
                       :spacing 10
                       :alignment :center-right
                       :grid-pane/column-index 0
                       :grid-pane/row-index 2
                       :children [(ui/label :text "Celsius")
                                  (ui/text-field
                                   :id :celsius
                                   :on-action {:on-enter :cel-far})])
                      (ui/h-box
                       :spacing 10
                       :alignment :center-right
                       :grid-pane/column-index 0
                       :grid-pane/row-index 3
                       :children [(ui/label :text "Fahrenheit")
                                  (ui/text-field
                                   :id :celsius
                                   :on-action {:on-enter :far-cel})])
                      (ui/label :text res
                                :grid-pane/column-index 0
                                :grid-pane/row-index 4)
                      (ui/label :text tres
                                :grid-pane/column-index 0
                                :grid-pane/row-index 5)])))

(defui Stage
  (render [this args]
          (ui/stage
           :title "Celsius <=> Fahrenheit"
           :shown true
           :scene (ui/scene
                   :root (translater args)))))

(defn -main []
  (let [data-state (atom {:res "Result" :tres "Sequence Time" :cel nil :far nil
                          :start-time nil})
        handler-fn (fn [{:keys [event] :as all-data}]
                     (if (= event :start-timer)
                       (swap! data-state assoc :start-time (System/currentTimeMillis))
                       (do (swap! data-state
                                  assoc :tres (str (- (System/currentTimeMillis)
                                                      (:start-time @data-state))))
                           (case event
                             :cer-far (swap! data-state
                                             assoc :res
                                             (+ 32 (* 1.8 (int (:cel @data-state)))))
                             :far-cel (swap! data-state
                                             assoc :res
                                             (/ (+ (- 0 32)
                                                   (int (:far @data-state))) 1.8)))))
                     (swap! data-state assoc :cel nil)
                     (swap! data-state assoc :far nil))
        ui-state (agent (dom/app (stage @data-state) handler-fn))]
    (add-watch data-state :ui
               (fn [_ _ _ _]
                 (send ui-state (fn [old]
                                  (dom/update-app old (stage @data-state))))))))

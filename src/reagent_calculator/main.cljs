(ns reagent-calculator.main
  [:require [reagent.core :as r]
   [reagent.dom :as rd]])

(defn represent-value [value]
  (cond
    (int? value) (str value)
    (= value 'addition) "+"
    (= value 'subtraction) "-"
    (= value 'multiplication) "*"
    (= value 'division) "/"
    (= value 'equals) "="
    :else "unknown"))

(defn swap-value-appender!
  [atom value]
  (swap! atom #(str % value)))

(defn handle-button [value left-value right-value operation result]
  (cond
    (int? value) (if (nil? @operation)
                   (swap-value-appender! left-value value)
                   (swap-value-appender! right-value value))
    ;; Shouldn't let the user enter an operation when there's nothing on the
    ;; left hand side
    (symbol? value) (when-not (nil? @left-value)
                      (reset! operation value))
    ;; TODO: handle equals sign
    ))

(defn build-buttons
  [left-value right-value operation result]
  (map
   (fn [values]
     ;; TODO: Printing is a placeholder
     [:div {:id "calculator-btn-row" :key values}
      (map (fn [value]
             [:button
              {:on-click #(handle-button value left-value right-value operation result) :key value}
              (represent-value value)])
           values)])
   ;; TODO: These probably aren't arranged well but it should be easy to change
   ;; this later
   [[1 2 3 'addition] [4 5 6 'subtraction] [7 8 9 'multiplication 'division] ['equals]]))

;; FIXME: I think there might be a better solution to this problem. Its just
;; that I can't think of one at present so this will have to do
(defn entry-box-representation
  [left-value right-value operation result]
  (cond
    (not (nil? result)) result
    (and (nil? operation) (not (nil? left-value))) left-value
    ;; TODO: convert operation to string.
    (not (and (nil? left-value) (nil? right-value) (nil? operation))) (str left-value " " operation " " right-value)
    :else ""))

(defn calculator
  []
  (let [left-value (r/atom nil)
        right-value (r/atom nil)
        operation (r/atom nil)
        result (r/atom nil)]
    (fn []
      [:div
       [:button {:on-click #(handle-button 2 left-value right-value operation result)}]
       [:input {:type "text" :value (entry-box-representation @left-value @right-value @operation @result)}]
       [:div {:id "btn-columns"}
        [:div {:id "calc-buttons"}
         (build-buttons left-value right-value operation result)]]])))

(defn base []
  [:div
   [:h1 "Reagent Calculator"]
   [calculator]])


(defn render
  []
  (rd/render [base]
             (.getElementById js/document "app")))

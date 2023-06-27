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

(defn build-buttons
  [left-value right-value operation result]
  (map
   (fn [values]
     ;; TODO: Printing is a placeholder
     [:div {:id "calculator-btn-row" :key values}
      (map (fn [value]
             [:button
              {:on-click #(js/console.log value) :key value}
              (represent-value value)])
           values)])
   ;; TODO: These probably aren't arranged well but it should be easy to change
   ;; this later
   [[1 2 3 'addition] [4 5 6 'subtraction] [7 8 9 'multiplication 'division] ['equals]]))

(defn calculator
  []
  (let [left-value (r/atom nil)
        right-value (r/atom nil)
        operation (r/atom nil)
        result (r/atom nil)]
    [:div
     [:input {:type "text"}]
     [:div {:id "btn-columns"}
      [:div {:id "calc-buttons"}
       (build-buttons left-value right-value operation result)]]]))

(defn base []
  [:div
   [:h1 "Reagent Calculator"]
   [calculator]])


(defn render
  []
  (rd/render [base]
             (.getElementById js/document "app")))

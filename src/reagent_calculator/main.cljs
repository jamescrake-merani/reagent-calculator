(ns reagent-calculator.main
  [:require [reagent.core :as r]
   [reagent.dom :as rd]])

(defn represent-value [value]
  (cond
    (int? value) (str value)
    (= value 'addition) "+"
    (= value 'subtraction) "-"
    (= value 'multiplication) "*"
    (= value 'equals) "="
    :else "unknown"))

(defn build-buttons
  []
  (map
   (fn [values]
     ;; TODO: Printing is a placeholder
     [:div {:id "calculator-btn-row" :key values}
      (map (fn [value]
             [:button
              {:on-click #(js/console.log value) :key value}
              (represent-value value)])
           values)])
   [[1 2 3] [4 5 6] [7 8 9]]))

(defn calculator
  []
  [:div
   [:input {:type "text"}]
   [:div {:id "btn-columns"}
    [:div {:id "calc-buttons"}
     (build-buttons)]]])

(defn base []
  [:div
   [:h1 "Reagent Calculator"]
   [calculator]])


(defn render
  []
  (rd/render [base]
             (.getElementById js/document "app")))

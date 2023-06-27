(ns reagent-calculator.main
  [:require [reagent.core :as r]
   [reagent.dom :as rd]])

(defn build-buttons
  []
  (map
   (fn [value]
     ;; TODO: Printing is a placeholder
     [:button {:on-click #(js/console.log value)} value])
   [1 2 3 4 5 6 7 8 9]))

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

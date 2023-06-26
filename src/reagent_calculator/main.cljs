(ns reagent-calculator.main
  [:require [reagent.core :as r]
   [reagent.dom :as rd]])

(defn calculator
  []
  [:div
   [:input {:type "text"}]])

(defn base []
 [:div
  [:h1 "Reagent Calculator"]
  [calculator]])


(defn render
  []
  (rd/render [base]
             (.getElementById js/document "app")))

(ns reagent-calculator.main
  [:require [reagent.core :as r]
   [reagent.dom :as rd]])

(defn base []
 [:div
  [:h1 "Placeholder Header"]])

(defn render
  []
  (rd/render [base]
             (.getElementById js/document "app")))

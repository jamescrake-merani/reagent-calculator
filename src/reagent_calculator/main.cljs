(ns reagent-calculator.main
  [:require [reagent.core :as r]
   [reagent.dom :as rd]
   [clojure.edn :as edn]])

;; TODO: There's quite a bit of reptition here; could come up with a better
;; solution

(defn represent-value [value]
  (cond
    (int? value) (str value)
    (= value 'addition) "+"
    (= value 'subtraction) "-"
    (= value 'multiplication) "*"
    (= value 'division) "/"
    (= value 'equals) "="
    (= value 'backspace) "Del"
    (= value ".") "."
    :else "unknown"))

(def operations ['addition 'subtraction 'multiplication 'division])

(defn operation? [x]
  (some #{x} operations))

(defn key-to-operation [key]
  (cond
    (= key "+") 'addition
    (= key "-") 'subtraction
    (or (= key "x") (= key "*")) 'multiplication
    (or (= key "/") (= key "÷")) 'division
    (or (= key "=") (= key "Enter")) 'equals
    (= key "Backspace") 'backspace
    :else nil
    ))

(defn swap-value-appender!
  [atom value]
  (swap! atom #(str % value)))

(defn calc-result
   [state]
  (let [left-value-num (edn/read-string @(:left-value state))
        right-value-num (edn/read-string @(:right-value state))
        operation @(:operation state)]
    (cond
      (= operation 'addition) (+ left-value-num right-value-num)
      (= operation 'subtraction) (- left-value-num right-value-num)
      (= operation 'multiplication) (* left-value-num right-value-num)
      (= operation 'division) (/ left-value-num right-value-num))))

;; Doesn't replace if there is already a result.
(defn swap-result!
  [state new-value]
  (swap! (:result state) (fn [old-value]
                  (if (nil? old-value)
                    new-value
                    old-value))))

(defn all-clear! [state]
  (doseq [value (vals state)]
    (reset! value nil)))

(defn backspace-value
  [value]
  (if (= 1 (count value))
    nil
    (clojure.string/join (drop-last value))))

(defn backspace
  [state]
  (if (and (some? @(:operation state)) (nil? @(:right-value state)))
    (reset! (:operation state) nil)
    (let [to-backspace (if (some? @(:right-value state))
                         (:right-value state)
                         (:left-value state))]
      (swap! to-backspace backspace-value))))

(defn result-into-new-calc
  [state value]
  (reset! (:left-value state) (str @(:result state)))
  (reset! (:operation state) nil)
  (reset! (:right-value state) nil)
  (reset! (:result state) nil))

(defn handle-button [state value]
  (cond
    (some? @(:result state)) (do
                               (if (operation? value)
                                 (result-into-new-calc state value)
                                 (all-clear! state))
                               (handle-button state value))
    (or (int? value) (= value ".")) (if (nil? @(:operation state))
                   (swap-value-appender! (:left-value state) value)
                   (swap-value-appender! (:right-value state) value))
    ;; Shouldn't let the user enter an operation when there's nothing on the
    ;; left hand side
    (= value 'backspace) (backspace state)
    (symbol? value) (when-not (nil? @(:left-value state))
                      (if (= value 'equals)
                        (swap-result! state (calc-result state))
                        (reset! (:operation state) value)))
    ;; TODO: handle equals sign
    ))

(defn build-buttons
  [state]
  (map
   (fn [values]
     [:div {:id "calculator-btn-row" :key values}
      (map (fn [value]
             [:button
              {:on-click #(handle-button state value) :key value}
              (represent-value value)])
           values)])
   ;; TODO: These probably aren't arranged well but it should be easy to change
   ;; this later
   [[1 2 3 'addition] [4 5 6 'subtraction] [7 8 9 'multiplication 'division] [0 'equals 'backspace "."]]))

;; FIXME: I think there might be a better solution to this problem. Its just
;; that I can't think of one at present so this will have to do
(defn entry-box-representation
  [state]
  (cond
    (not (nil? @(:result state))) (str "= " @(:result state))
    (and (nil? @(:operation state)) (not (nil? @(:left-value state)))) @(:left-value state)
    (not (and (nil? @(:left-value state)) (nil? @(:right-value state)) (nil? @(:operation state)))) (str @(:left-value state) " " (represent-value @(:operation state)) " " @(:right-value state))
    :else ""))

(defn parse-input [input]
  (let [parsed-long (parse-long input)]
    (cond parsed-long parsed-long
          (= input ".") input
          :else nil)))

(defn on-typed-input
  [event state]
  (let [input (-> event .-key)
        parsed-input (parse-input input)]
    (if parsed-input
      (handle-button state parsed-input)
      (let [operation (key-to-operation input)]
        (when-not (nil? operation)
          (handle-button state operation))))))

(defn calculator
  []
  (let [state {:left-value (r/atom nil)
               :right-value (r/atom nil)
               :operation (r/atom nil)
               :result (r/atom nil)}]
    (fn []
      [:div
       [:input {:type "text" :value (entry-box-representation state) :on-key-down #(on-typed-input % state)}]
       [:div {:id "btn-columns"}
        [:div {:id "calc-buttons"}
         (build-buttons state)]]])))

(defn base []
  [:div
   [:h1 "Reagent Calculator"]
   [calculator]])


(defn render
  []
  (rd/render [base]
             (.getElementById js/document "app")))

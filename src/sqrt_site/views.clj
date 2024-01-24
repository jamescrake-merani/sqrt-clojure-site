(ns sqrt-site.views
  (:require [hiccup2.core :as h]
            [ring.util.anti-forgery :as af]
            [sqrt-site.newton :as newton]
            [clojure.math :as math]
            [sqrt-site.verification :as verify]
            [clojure.pprint :refer [cl-format]]))

(defn template [current-page]
  (h/html
   [:html {:lang "en"}
    [:head
     [:link {:rel "stylesheet"
             :href "https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
             :integrity "sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN"
             :crossorigin "anonymous"}]]
    [:body
     [:div.container
      [:h1.text-center "Square Root with the Newton Method"]
      [:a {:href "/"} "Home"]
      " "
      [:a {:href "/about"} "About"]
      [:hr]
      current-page]]]))

(defn error-alert [error-msg]
  (h/html
   [:div.alert.alert-danger
    error-msg]))

(defn format-default-value [x]
  (cl-format nil "~D" x))

(defn sqrt-form [initial-to-sqrt initial-precision]
  (h/html
   [:form {:method "POST"}
    (h/raw (af/anti-forgery-field))
    [:div.row
     [:div.col
      [:label.form-label {:for "sqrt-input"} "Number to square root:"]
      [:input.form-control
       {:id "sqrt-input"
        :type "number"
        :name "to-sqrt"
        :placeholder "Enter a number find the square root of"
        :required true
        :value (format-default-value  initial-to-sqrt)
        :step :any
        :max verify/to-sqrt-max
        :min verify/to-sqrt-min}]]
     [:div.col
      [:label.form-label {:for "precision-input"} "Precision:"]
      [:input.form-control
       {:id "precision-input"
        :type "number"
        :name "precision"
        :placeholder "Enter the precision"
        :value (format-default-value initial-precision)
        :required true
        :step :any
        :max verify/precision-max
        :min verify/precision-min}]]]
    [:button.btn.btn-primary {:type "submit"} "Calculate"]]))

(defn value-card [value to-sqrt precision attempt-no]
  [:div.card {:class (if (newton/good-enough? value to-sqrt precision)
                       "text-bg-success"
                       "text-bg-secondary")}
   [:div.card-header "Attempt #" attempt-no]
   [:div.card-body
    [:p.card-text (format "√%s ≈ %s" to-sqrt value)
     [:br]
     (cl-format nil "± ~D" (abs (- (math/pow value 2) to-sqrt)))]]])


(defn sqrt-calc-view [to-sqrt precision sqrt-steps]
  (doall
   (map #(value-card %1 to-sqrt precision %2) sqrt-steps (range 1 (inc (count sqrt-steps))))))

(defn home-view [to-sqrt precision sqrt-steps error-msg]
  (template
   (h/html
    [:div
     [:h1 "Calculation Form"]
     [:p "Enter a number to square root, and the precision desired. Then click
     calculate to see the estimations for the square root of that number."]
     (sqrt-form to-sqrt precision)
     [:hr]
     (when-not (nil? sqrt-steps)
       (sqrt-calc-view to-sqrt precision sqrt-steps))
     (when-not (nil? error-msg)
       (error-alert error-msg))])))

(defn about-view []
  (template
   (h/html
    [:div
     [:h1 "About Page"]
     [:p "This will eventually have a description of Newton's method of
     calculating the sqrt but right now is empty"]])))

(ns sqrt-site.views
  (:require [hiccup2.core :as h]
            [ring.util.anti-forgery :as af]
            [sqrt-site.newton :as newton]
            [clojure.math :as math]))

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
      [:h1.text-center "Square Root Website"]
      [:hr]
      current-page]]]))

(defn sqrt-form [initial-to-sqrt initial-precision]
  (h/html
   [:form {:method "POST"}
    (h/raw (af/anti-forgery-field))
    [:input.form-control {:type "number" :name "to-sqrt" :placeholder "Enter a number find the square root of" :value initial-to-sqrt}]
    [:input.form-control {:type "number" :name "precision" :placeholder "Enter the precision" :value initial-precision :step :any :max 1 }]
    [:button.btn.btn-primary {:type "submit"} "Calculate"]]))

(defn value-card [value to-sqrt precision]
  [:div.card {:class (if (newton/good-enough? value to-sqrt precision)
                       "text-bg-success"
                       "text-bg-secondary")}
   [:div.card-header "Attempt"]
   [:div.card-body
    [:p.card-text (format "√%s ≈ %s" to-sqrt value)] ]])


(defn sqrt-calc-view [to-sqrt precision]
  (let [sqrt-steps (newton/sqrt to-sqrt precision)]
    (doall
     (map #(value-card % to-sqrt precision) sqrt-steps))))

(defn home-view [to-sqrt precision]
  (template
   (h/html
    [:div
     [:h1 "This is the home page"]
     [:p "It doesn't have anything on it at the moment."]
     (sqrt-form to-sqrt precision)
     (when-not (nil? to-sqrt)
       [:div
        [:hr]
        (sqrt-calc-view (parse-double to-sqrt) (parse-double precision))])])))

(defn about-view []
  (template
   (h/html
    [:div
     [:h1 "About Page"]
     [:p "This will eventually have a description of Newton's method of
     calculating the sqrt but right now is empty"]])))

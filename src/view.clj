(ns sqrt-site.views
  (:require [hiccup2.core :as h]))

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
      [:h1 "Template"]
      [:hr]
      current-page]]]))

(defn home-view []
  (template
   (h/html
    [:div
     [:h1 "This is the home page"]
     [:p "It doesn't have anything on it at the moment."]])))

(defn about-view []
  (template
   (h/html
    [:div
     [:h1 "About Page"]
     [:p "This will eventually have a description of Newton's method of
     calculating the sqrt but right now is empty"]])))

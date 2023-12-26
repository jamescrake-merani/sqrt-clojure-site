(ns sqrt-site.main
  (:gen-class)
  (:require [ring.adapter.jetty :as jetty]
            [reitit.ring :as ring]
            [ring.util.http-response :as response]
            [ring.middleware.defaults :as defaults]))

(defonce server (atom nil))

(defn html-response [content]
  (-> (response/ok content)
      (response/content-type "text/html")))

(defn home-page [request]
  (html-response "<h1>Home Page</h1>"))

(defn about-page [request]
  (html-response "<h1>About Page</h1>"))

(def handler
  (ring/ring-handler
   (ring/router
    [["/" {:get home-page}]
     ["/about" {:get about-page}]])))

(defn start! []
  (reset!
   server
   (jetty/run-jetty (-> #'handler (defaults/wrap-defaults defaults/site-defaults) )
                    {:join? false
                     :port 8080})))

(defn stop! []
  (.stop @server)
  (reset! server nil))

(defn -main [& args]
  (start!))

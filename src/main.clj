(ns sqrt-site.main
  (:gen-class)
  (:require [ring.adapter.jetty :as jetty]))

(defonce server (atom nil))

(defn handler [request]
  {:status 200
   :body "To be implemented"})

(defn start! []
  (reset!
   server
   (jetty/run-jetty (-> #'handler) {:join? false
                                    :port 8080})))

(defn stop! []
  (.stop @server)
  (reset! server nil))

(defn -main [& args]
  (start!))

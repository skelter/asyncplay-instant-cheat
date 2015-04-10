(ns asyncplay.core
  (:require [clojure.core.async :as async :refer [go chan <! <!! >! >!!]]
            [org.httpkit.client :as http]))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(def memlog (agent (list)))

(defn hget
  "Fetch the contents of the url, async.  Returns a channel from which the results will be gotten."
  [url]
  (let [ch (chan)]
    (go (>! ch @(http/get url)))
    (go (send memlog conj (<! ch)))))

(defn hgetbunch
  "Fetch a bunch of urls. Returns seq of channels."
  [n]
  (for [i (range n)]
    (hget "http://www.skelter.net/")))

(defn dump
  "Prints fetched from channels."
  []
  (for [ch (hgetbunch 1000)]
    (<!! ch)))


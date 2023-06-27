(ns bhlie.udemy.todo.app
  (:require [reagent.dom.client :refer [create-root]]
            [reagent.core :as r]
            [goog.dom :as gdom]
            [re-frame.core :as rf]
            [bhlie.udemy.todo.events]
            [bhlie.udemy.todo.views :refer [todo-list]]
            ["react" :refer [StrictMode]]))

(defonce root (create-root (gdom/getElement "root")))

(defn render []
  (.render root (r/as-element [:> StrictMode [todo-list]])))

(defn ^:dev/after-load clear-cache-and-render! []
  (rf/clear-subscription-cache!)
  (render))

(defn ^:export main []
  (rf/dispatch-sync [:initialize-app])
  (render))
(ns bhlie.udemy.todo.events
  (:require [re-frame.core :refer [reg-event-fx reg-event-db ->interceptor]]
            [bhlie.udemy.todo.config]
            [clojure.spec.alpha :as s]))

(def check-valid-db?
  (->interceptor
   :id :valid-db?
   :after (fn [context] (let [todos (get-in context [:effects :db :todos])
                              valid? (s/valid? :bhlie.udemy.todo.config/db-spec todos)]
                          (when-not valid? (throw (js/Error. "your db is messed up, man!")))))))

(reg-event-fx
 :initialize-app
 (fn [_ _]
   {:db {:todos [{:name "Feed the cats" :desc "FOOD" :done? true}]
         :editing? false}}))

(reg-event-db
 :add-todo
 [check-valid-db?]
 (fn [{:keys [todos] :as db} [_ task]]
   (assoc db :todos (conj todos task))))

(reg-event-fx
 :update-todo
 [check-valid-db?]
 (fn [{:keys [db]} [_ {:keys [name done?] :as new-task}]]
   (let [old-task (->> db :todos (filter (fn [task] (-> task :name (= name)))) first)
         updated-tasks (assoc db :todos (conj (:todos db) (merge old-task new-task {:done? done?})))]
     {:db updated-tasks
      :fx [[:dispatch [:editing-todo]]]})))

(reg-event-db
 :delete-todo
 [check-valid-db?]
 (fn [{:keys [todos] :as db} [_ {:keys [name]}]]
   (->> todos
        (into [] (remove (fn [task] (-> task :name (= name)))))
        (assoc db :todos))))

(reg-event-db
 :editing-todo
 (fn [db _]
   (update db :editing? not)))
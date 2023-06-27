(ns bhlie.udemy.todo.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 :app/show-todos
 (fn [db]
   (get db :todos)))

(reg-sub
 :app/show-pending
 :<- [:app/show-todos]
 (fn [db]
   (into [] (comp (filter (fn [task] (-> task :done? false?)))
                  (map :name)) db)))

(reg-sub
 :app/show-done
 :<- [:app/show-todos]
 (fn [db]
   (into []
         (comp (filter (fn [task] (-> task :done? true?)))
               (map :name)) db)))

(reg-sub
 :todos/editing?
 :=> :editing?)
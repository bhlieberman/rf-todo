(ns bhlie.udemy.todo.views
  (:require [re-frame.core :refer [dispatch subscribe]]
            [bhlie.udemy.todo.subs]
            [bhlie.udemy.todo.events]))

(defn delete-task [^js e] (let [task (.. e -target -parentNode -firstElementChild -innerText)]
                        (dispatch [:delete-todo {:name task}])))

(defn update-task [^js e] (let [new-task (.. e -target -editBox -value)]
                        (.preventDefault e)
                        (dispatch [:update-todo {:name new-task
                                                 :done? true}])))

(defn show-todos []
  (let [todos @(subscribe [:app/show-pending])
        editing? @(subscribe [:todos/editing?])]
    [:div (for [todo todos]
            [:div {:key (random-uuid)
                   :id "todo-container"}
             (if editing?
               [:form {:on-submit update-task} 
                [:label {:for "editBox"} "Update: "] 
                [:input {:name "editBox"}]
                [:button {:type "submit"}
                 [:i {:class "fa-solid fa-check"}]]]
               [:span todo])
             [:i {:class "fa-solid fa-pencil"
                  :on-click (fn [_] (dispatch [:editing-todo]))}]
             [:i {:class "fa-solid fa-trash deleteTodo"
                  :on-click delete-task} "X"]])]))

(defn show-finished []
  (let [done @(subscribe [:app/show-done])
        editing? @(subscribe [:todos/editing?])]
    [:div (for [d done]
            [:div {:key (random-uuid)
                   :id "todo-container"}
             (if editing?
               [:form {:on-submit update-task}
                [:label {:for "editBox"} "Update: " d " with"]
                [:input {:name "editBox"}]
                [:button {:type "submit"}
                 [:i {:class "fa-solid fa-check"}]]]
               [:div [:span d]
                [:i {:class "fa-solid fa-pencil"
                     :on-click (fn [_] (dispatch [:editing-todo]))}]
                [:i {:class "fa-solid fa-trash deleteTodo"
                     :on-click delete-task}]])])]))

(defn add-todo-task []
  [:form {:on-submit (fn [^js e] (let [name (.. e -target -taskName -value)
                                       desc (.. e -target -taskDesc -value)
                                       done? (.. e -target -taskDone -checked)]
                                   (.preventDefault e)
                                   (dispatch [:add-todo {:name name :desc desc :done? done?}])))}
   [:label {:for "taskName"} "Task: "]
   [:input {:name "taskName"}]
   [:label {:for "taskDesc"} "Description: "]
   [:input {:name "taskDesc"}]
   [:label {:for "taskDone"} "Done?"]
   [:input {:type "checkbox" :name "taskDone"}]
   [:button "Add task"]])

(defn todo-list []
  [:div {:id "todoList"}
   [:div {:id "done"} [:p {:style {:font-weight :bold}} "Done:"] [show-finished]]
   [:div {:id "pending"} [:p {:style {:font-weight :bold}} "Doing: "] [show-todos]]
   [:div {:id "add-new"} [:p {:style {:font-weight :bold}} "Add new: "] [add-todo-task]]])
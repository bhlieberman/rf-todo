(ns bhlie.udemy.todo.config
  (:require [clojure.spec.alpha :as s]))

(s/def ::name string?)

(s/def ::desc string?)

(s/def ::done? boolean?)

(s/def ::todo-spec (s/keys :req-un [::name ::done?]
                           :opt-un [::desc]))

(s/def ::db-spec (s/coll-of ::todo-spec))
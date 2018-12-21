(ns app.webgl.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :webgl/initialized?
  (fn [db _]
    (:initialized? (:webgl db))))

(reg-sub
  :webgl/visible?
  (fn [db _]
    (:visible? (:webgl db))))

(reg-sub
  :webgl/mounted?
  (fn [db _]
    (:mounted? (:webgl db))))

(ns app.webgl.core
  (:require [three :as THREE]
            [app.webgl.subs]
            [app.webgl.events]))

;; Instantiate global var for current requestAnimationFrame id
(set! js/RAF nil)

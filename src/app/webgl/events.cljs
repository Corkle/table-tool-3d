(ns app.webgl.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx reg-fx dispatch]]
            [three :as THREE]
            [three-orbitcontrols :as ORBIT-CONTROLS]))

(reg-event-db
  :webgl/visible
  (fn [db [_ value]]
    (assoc-in db [:webgl :visible?] value)))

(reg-event-fx
  :webgl/initialize
  (fn [cofx _]
    {:db (assoc-in (:db cofx) [:webgl :initialized?] true)
     :initialize-webgl-fx nil}))

(reg-event-fx
  :webgl/mount
  (fn [cofx [_ element]]
    {:db (assoc-in (:db cofx) [:webgl :mounted?] true)
     :mount-webgl-fx element}))

(defn- animate!
  []
  (let []
    (set! js/RAF (js/requestAnimationFrame animate!))
    (.render js/RENDERER js/SCENE js/CAMERA)))

(defn- cancel-animate!
  []
  (if-let [id js/RAF]
    (do
      (js/cancelAnimationFrame id)
      (set! js/RAF nil))))

(defn- create-sphere
  [color]
  (let [geometry (THREE/SphereBufferGeometry. 0.1 32 32)
        material (THREE/MeshStandardMaterial. #js {:color 0x000000
                                                   :emissive color})]
    (THREE/Mesh. geometry material)))

(defn- create-point-light
  [color [x y z]]
  (let [light (THREE/PointLight. color 1.5 20)
        sphere (create-sphere color)]
    (.set (.-position light) x y z)
    (set! (.-castShadow light) true)
    (set! (.-near (.-camera (.-shadow light))) 0.5)
    (set! (.-far (.-camera (.-shadow light))) 60)
    (.add light sphere)

    light))

(defn- create-box
  [size [x y z]]
  (let [geometry (THREE/BoxBufferGeometry. size size size)
        material (THREE/MeshStandardMaterial. #js {:color 0xa0adaf
                                                   :metalness 0.75
                                                   :roughness 0.75})
        box (THREE/Mesh. geometry material)]
    (set! (.-x (.-position box)) x)
    (set! (.-y (.-position box)) y)
    (set! (.-z (.-position box)) z)
    (set! (.-receiveShadow box) true)
    (set! (.-castShadow box) true)
    box))

(defn- create-plane
  []
  (let [geometry (THREE/PlaneGeometry. 900, 900)
        material (THREE/MeshPhongMaterial. #js {:color 0x11a11d
                                                :shininess 10
                                                :specular 0x111111
                                                :side THREE/DoubleSide})
        plane (THREE/Mesh. geometry material)]
    (set! (.-x (.-rotation plane)) (/ (.-PI js/Math) -2))
    (set! (.-receiveShadow plane) true)
    plane))

(defn- create-orbit-controls!
  [camera renderer]
  (let [controls (ORBIT-CONTROLS. camera (.-domElement renderer))]
    (set! (.-minDistance controls) 0)
    (set! (.-maxDistance controls) 100)
    (set! (.-maxPolarAngle controls) (/ (.-PI js/Math) 2))
    (set! (.-panSpeed controls) 0.25)
    (set! (.-rotateSpeed controls) 0.25)
    ;(set! (.-minAzimuthAngle controls) (/ (.-PI js/Math) -4))
    ;(set! (.-maxAzimuthAngle controls) (/ (.-PI js/Math) 4))
    (.set (.-target controls) 0 1 0)
    controls))

(defn- test-scene
  []
  (let [scene js/SCENE 
        light (create-point-light 0x0088ff [0 1.5 0])
        light2 (create-point-light 0xff8822 [2 2 2])
        amb-light (THREE/AmbientLight. 0x111111)
        box (create-box 0.75 [1 2 1])
        box2 (create-box 0.25 [-1 2.8 -1])
        box3 (create-box 0.25 [2.5 0.5 -1])
        box4 (create-box 1 [0.5 0.5 0.5])
        plane (create-plane)]

      (.add scene amb-light)
      (.add scene light)
      (.add scene light2)
      (.add scene box)
      (.add scene box2)
      (.add scene box3)
      (.add scene box4)
      (.add scene plane)))

(reg-fx
  :initialize-webgl-fx
  (fn [_]
    (let [renderer (THREE/WebGLRenderer. #js {:antialias true})
          window-w (- (.-innerWidth js/window) 80)
          window-h (.-innerHeight js/window)
          scene (THREE/Scene.)
          camera (THREE/PerspectiveCamera. 45 (/ window-w window-h) 1 1000)
          viewport (.getElementById js/document "webgl-container")
          controls (create-orbit-controls! camera renderer)
          grid (THREE/GridHelper. 200 200 0x637F6F 0x637F6F)]
      (set! (.-y (.-position camera)) 5)
      (set! (.-z (.-position camera)) 10)
      (.update controls)

      (.add scene grid)
      
      (.setSize renderer window-w window-h)
      (set! (.-enabled (.-shadowMap renderer)) true)
      (set! (.-type (.-shadowMap renderer)) THREE/BasicShadowMap)

      (set! js/RENDERER renderer)
      (set! js/SCENE scene)
      (set! js/CAMERA camera)
      (set! js/CONTROLS controls)
      
      (test-scene))))

(reg-fx
  :mount-webgl-fx
  (fn [element]
    (let [viewport (.getElementById js/document element)
          canvas (.-domElement js/RENDERER)]
      (if-not (.contains viewport canvas)
        (.appendChild viewport canvas))
      (cancel-animate!)
      (animate!))))

(ns app.tokens-menu.db)

(defn- token
  [id name img]
  {:id id
   :name name
   :img img})

(def blue-cube
  (token 0 "Blue Cube" "http://www.i2clipart.com/cliparts/3/a/a/9/1281353aa9352f1b186595d501615cd23fa56a.png"))

(def green-cube 
  (token 1 "Green Cube" "http://www.i2clipart.com/cliparts/3/a/a/9/1280453aa9352f1b186595d501615cd23fa56a.png"))

(def red-cube
  (token 2 "Red Cube" "http://www.i2clipart.com/cliparts/3/a/a/9/1282703aa9352f1b186595d501615cd23fa56a.png"))

(def purple-cube
  (token 3 "Purple Cube" "http://www.i2clipart.com/cliparts/3/a/a/9/1281803aa9352f1b186595d501615cd23fa56a.png"))

(def teal-cube
  (token 4 "Teal Cube" "http://www.i2clipart.com/cliparts/3/a/a/9/1280903aa9352f1b186595d501615cd23fa56a.png"))

(def brown-cube
  (token 5 "Brown Cube" "http://www.i2clipart.com/cliparts/3/a/a/9/1283153aa9352f1b186595d501615cd23fa56a.png"))

(def tokens-menu-db
  {:pos [95 25]
   :selected-item nil
   :tokens-list [blue-cube
                 green-cube
                 red-cube
                 purple-cube
                 teal-cube
                 brown-cube]
   :visible? true})

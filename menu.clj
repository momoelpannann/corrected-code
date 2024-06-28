(ns menu
  (:require [clojure.string :as str]
            [db]))

(defn showMenu []
  (println "\n\n*** City Information Menu ***")
  (println "-----------------------------\n")
  (println "1. List cities")
  (println "2. Display City Information")
  (println "3. List Provinces")
  (println "4. Display Province Information")
  (println "5. Exit")
  (do 
    (print "\nEnter an option? ") 
    (flush) 
    (read-line)))

(defn option1 [cities-db]
  (println "*** List Cities Submenu ***")
  (println "1. List all cities")
  (println "2. List all cities for a given province, ordered by size and name")
  (println "3. List all cities for a given province, ordered by population density")
  (println "4. Back to main menu")
  (print "Enter an option: ")
  (flush)
  (let [sub-option (read-line)]
    (cond
      (= sub-option "1") (doseq [city (db/get-cities cities-db)]
                           (println (:name city)))
      (= sub-option "2") (do
                           (print "Enter province name: ")
                           (flush)
                           (let [province (read-line)]
                             (doseq [city (db/get-cities-by-province cities-db province)]
                               (println (str (:name city) " " (:size city) " " (:population city))))))
      (= sub-option "3") (do
                           (print "Enter province name: ")
                           (flush)
                           (let [province (read-line)]
                             (doseq [city (db/get-cities-by-density cities-db province)]
                               (println (str (:name city) " " (:size city) " " (double (/ (:population city) (:area city))))))))
      (= sub-option "4") (println "Returning to main menu")
      :else (println "Invalid option"))))

(defn option2 [cities-db]
  (print "\nPlease enter the city name => ") 
  (flush)
  (let [city-name (read-line)]
    (if-let [city (db/get-city-info cities-db city-name)]
      (println city)
      (println "City not found."))))

(defn option3 [cities-db]
  (println "List all provinces with total number of cities")
  (doseq [[province count] (db/get-provinces cities-db)]
    (println (str province " " count)))
  (let [total-cities (reduce + (map second (db/get-provinces cities-db)))
        total-provinces (count (db/get-provinces cities-db))]
    (println (str "Total: " total-provinces " provinces, " total-cities " cities on file."))))

(defn option4 [cities-db]
  (println "List all provinces with total population")
  (doseq [[province population] (db/get-provinces-population cities-db)]
    (println (str province " " population))))

(defn processOption [option cities-db]
  (cond
    (= option "1") (option1 cities-db)
    (= option "2") (option2 cities-db)
    (= option "3") (option3 cities-db)
    (= option "4") (option4 cities-db)
    :else (println "Invalid Option, please try again")))

(defn main-menu [cities-db]
  (loop []
    (let [option (str/trim (showMenu))]
      (if (= option "5")
        (println "\nGood Bye\n")
        (do 
          (processOption option cities-db)
          (recur))))))

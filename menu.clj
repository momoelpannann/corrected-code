;; menu.clj
;; This file contains functions for displaying the main menu and handling user interaction.
;; It provides various options to list cities, display city information, list provinces, 
;; and display province information. The `main-menu` function drives the menu loop, 
;; repeatedly prompting the user for input and processing the selected option.

(ns menu
  (:require [clojure.string :as str]
            [db]))

;; Function to display the main menu and prompt the user for an option.
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

;; Function to handle the submenu for listing cities.
;; Update: Accept `cities-db` as a parameter
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
      ;; Update: Use `cities-db` instead of `db/cities-db`
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

;; Function to display information for a specific city.
;; Update: Accept `cities-db` as a parameter
(defn option2 [cities-db]
  (print "\nPlease enter the city name => ") 
  (flush)
  (let [city-name (read-line)]
    ;; Update: Use `cities-db` instead of `db/cities-db`
    (if-let [city (db/get-city-info cities-db city-name)]
      (println city)
      (println "City not found."))))

;; Function to list all provinces with the total number of cities.
;; Update: Accept `cities-db` as a parameter
(defn option3 [cities-db]
  (println "List all provinces with total number of cities")
  ;; Update: Use `cities-db` instead of `db/cities-db`
  (doseq [[province count] (db/get-provinces cities-db)]
    (println (str province " " count)))
  (let [total-cities (reduce + (map second (db/get-provinces cities-db)))
        total-provinces (count (db/get-provinces cities-db))]
    (println (str "Total: " total-provinces " provinces, " total-cities " cities on file."))))

;; Function to list all provinces with the total population.
;; Update: Accept `cities-db` as a parameter
(defn option4 [cities-db]
  (println "List all provinces with total population")
  ;; Update: Use `cities-db` instead of `db/cities-db`
  (doseq [[province population] (db/get-provinces-population cities-db)]
    (println (str province " " population))))

;; Function to process the selected option from the main menu.
;; Update: Accept `cities-db` as a parameter
(defn processOption [option cities-db]
  (cond
    ;; Update: Pass `cities-db` to each option function call
    (= option "1") (option1 cities-db)
    (= option "2") (option2 cities-db)
    (= option "3") (option3 cities-db)
    (= option "4") (option4 cities-db)
    :else (println "Invalid Option, please try again")))

;; Function to drive the main menu loop, repeatedly prompting the user for input and processing the selected option.
;; Update: Rename to `main-menu` and accept `cities-db` as a parameter
(defn main-menu [cities-db]
  ;; Add a loop to repeatedly prompt the user for input and process the selected option
  (loop []
    (let [option (str/trim (showMenu))]
      (if (= option "5")
        (println "\nGood Bye\n")
        (do 
          ;; Update: Pass `cities-db` to `processOption`
          (processOption option cities-db)
          ;; Recur to prompt the user again
          (recur))))))

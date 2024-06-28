;; db.clj
(ns db
  (:require [clojure.string :as str]))

(defn parse-city [line]
  ;; Parses a line from the cities.txt file into a city map.
  (let [[name province size population area] (str/split line #"\|")]
    {:name name
     :province province
     :size size
     :population (read-string population)
     :area (read-string area)}))

(defn load-data [filename]
  ;; Loads and parses the city data from a file.
  (let [lines (str/split-lines (slurp filename))]
    (map parse-city lines)))

(defn get-cities [cities-db]
  ;; Returns a list of cities sorted by name.
  (sort-by :name cities-db))

(defn get-cities-by-province [cities-db province]
  ;; Returns a list of cities for a given province, sorted by size and name.
  (sort-by (juxt :size :name)
           (filter #(= (:province %) province) cities-db)))

(defn get-cities-by-density [cities-db province]
  ;; Returns a list of cities for a given province, sorted by population density.
  ;; Population density is calculated as population divided by area.
  (sort-by #(double (/ (:population %) (:area %)))
           (filter #(= (:province %) province) cities-db)))

(defn get-city-info [cities-db city-name]
  ;; Returns information for a specific city by name.
  (first (filter #(= (:name %) city-name) cities-db)))

(defn get-provinces [cities-db]
  ;; Returns a list of provinces with the total number of cities, sorted by the number of cities.
  (->> cities-db
       (group-by :province)
       (map (fn [[k v]] [k (count v)]))
       (sort-by second >)))

(defn get-provinces-population [cities-db]
  ;; Returns a list of provinces with the total population, sorted by province name.
  (->> cities-db
       (group-by :province)
       (map (fn [[k v]] [k (reduce + (map :population v))]))
       (sort-by first)))
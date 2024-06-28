(ns app
  (:require [db]
            [menu]))

;; Load the city data and start the main menu.
(let [cities-db (db/load-data "cities.txt")]
  (menu/main-menu cities-db))

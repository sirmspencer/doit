#!/usr/bin/env bb
(ns go
  (:require [clojure.string :as str]))

;; Add code

(defn file->vec [fname]
  (-> (slurp fname)
      (str/split #"\n")))

(defn cmds->map [cmds]
  (reduce (fn [acc cur]
            (let [[task & rest] cur
                  [just-task] (str/split task #"[ :]+")
                  all-rest (-> (apply str rest)
                               str/trim)]
              (assoc acc just-task all-rest)))
          {}
          cmds))

(defn read-do-file [fname]
  (let [lines (->> (file->vec fname)
                   (filterv (complement str/blank?)))
        cmd-starts (->> lines
                        (map-indexed (fn [i l]
                                       (when (re-find #"^[a-zA-Z]+" l)
                                         i)))
                        (remove nil?))
        cmd-groups (loop [cmd-lines cmd-starts
                          cmd []]
                     (let [[start end] cmd-lines
                           this-command (if end
                                          (subvec lines start end)
                                          (subvec lines start))
                           cmds (conj cmd this-command)]
                       (if (< 1 (count cmd-lines))
                         (recur (rest cmd-lines)
                                cmds)
                         cmds)))
        override-cmds (->> cmd-groups
                           (filterv (fn [l] (-> l first (str/includes? "override"))))
                           cmds->map)
        reg-cmds     (->> (filterv (fn [l] (-> l first (str/includes? "override") not))
                                   cmd-groups)
                          cmds->map)]
    [override-cmds reg-cmds]))

(let [[home-or home-cmds] (read-do-file "home.do")
      [project-or project-cmds] (read-do-file "project.do")]
  (merge home-cmds project-cmds home-or project-or))

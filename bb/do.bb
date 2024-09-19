#!/usr/bin/env bb
(ns do
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
              (when (str/includes? all-rest (str "^" just-task))
                (throw (ex-info (str "Cannot reference self in task: " just-task ": " all-rest) {})))
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

(defn read-config [{:keys [me?]}]
  (let [[home-or home-cmds] (read-do-file "home.do")
        [project-or project-cmds] (read-do-file "project.do")
        combined (if me?
                   (merge project-cmds home-cmds project-or home-or)
                   (merge home-cmds project-cmds home-or project-or))]
    (->> combined
         vec
         (sort-by first #(compare (count %2) (count %1))))))

(defn do-replace [config mystr]
  (loop [mystr mystr]
    (let [replaced (reduce (fn [acc [find replace]]
                             (str/replace acc (str "^" find) replace))
                           mystr
                           config)]
      (if (str/includes? replaced "^")
        (recur replaced)
        replaced))))

(def known-commands
  {:help {:param-count 1
          :key :help
          :help "-h --help will display help"}
   :me   {:param-count 1
          :key :me
          :help "-m --me will change the merge order to favor home tasks"}
   :echo {:param-count 1
          :key :echo
          :help "-e --echo will print the command without running it"}})

(defn get-command [cmd]
  (case cmd
    ("-h" "--help") (:help known-commands)
    ("-m" "--me") (:me known-commands)
    ("-e" "--echo") (:echo known-commands)
    nil))

(defn get-params [v]
  (loop [str-bits v
         params []]
    (let [f (first str-bits)
          {:keys [param-count]} (get-command f)]
      (if param-count
        (let [param (take param-count str-bits)]
          (recur (drop param-count str-bits)
                 (conj params param)))
        {:params params
         :cmd (str/join " " str-bits)}))))

(defn contains-command? [v param]
  (some (fn [c] (->> c
                     first
                     get-command
                     :key
                     (= param))) v))

(defn print-help []
  (println "------- do help -------\n")
  (doseq [[_ {:keys [help]}] known-commands]
    (println help)
    (println "")))

; "^task1 --help ^task44 ^task3"

(let [cli-input *command-line-args*
      {:keys [params cmd]} (get-params cli-input)
      me? (contains-command? params :me)
      config (read-config {:me? me?})
      cmd-replaced (do-replace config cmd)]
  (cond
    (contains-command? params :help)
    (print-help)

    (contains-command? params :echo)
    (println cmd-replaced)

    :else
    cmd-replaced))

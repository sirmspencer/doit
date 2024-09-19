task1:
  clojure -M:project/1

task2:
  clojure -M:project/2

task3 ^override:
  clojure -M:project/3

task4: ^override:
  clojure -M:project/4

params1:
  --run home

alias1:
  :env/home

taskA:
  clojure -M:home/A

taskB:
	clojure -M:home/B

task1:
  clojure -M:home/1

task2 ^override:
  clojure -M:home/2

task3:
  clojure -M:home/3

task4 ^override:
  clojure -M:home/4

params1:
  --run home

alias1:
  :env/home

combined1:
  ^alias1 ^params1

multiline1:
  mycmd
  --param 1

multiline2:
  mycmd
  && mycmd1

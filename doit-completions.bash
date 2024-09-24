_doit_complete() {
  cur="${COMP_WORDS[COMP_CWORD]}"
  prev="${COMP_WORDS[COMP_CWORD-1]}"
  echo $cur
  echo $prev
  printf -v "look at me"
  COMPREPLY=("test")
}

complete -F _doit_complete doit

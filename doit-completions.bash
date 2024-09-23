_doit_complete {
  printf -v "look at me"
  COMPREPLY=("test")
}

complete -F _doit_complete doit

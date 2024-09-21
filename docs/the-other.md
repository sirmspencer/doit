### bashrc

Lets looks at bashrc

```bash
alias lls=ls
```

You can call `lls -lash` like if you were calling `ls -lash`.  What you cant do is tie this to a project / repo.

```bash
alias ccm=clojure -M
```

You cant do  `ccm:my-repl`.

### make file

```make
example:
  ls
```

This wont work `make example -lash` because the `-lash` is a param to `npm` not `ls`. Taking in params can be done with variables but are annoyingly specific to each call, and more verbose.

### package.json

same as above

```json
  "scripts": {
    "lss": "ls",
  },
```

`npm run lss` is fine

`npm run lss -lash` doesn't work because the `-lash` is a param to `npm` not `ls`.

### Clojure specific examples

We have a deps.edn where every alias is defined independently instead of a large specific alias.

{:aliases {:env {:example "x"}}}

then we get calls like

clojure -M:env

And a makefile could be

```make
dev:
  clojure -M:env
```

For calling `make dev`

The issues is that you cant then do `make dev --watch` or `make dev:cider`

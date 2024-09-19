# do

a purposely simple aliasing tool

## do what?

Well anything and nothing.

A system to do aliasing that is both based on an individual home alias config merged with a project specific alias config.

This will be an aliasing tool so simple it can do anything.  The idea here is to alias the first bit of any cli call and then also allow for any additional part of a cli call to be added.

```do
my-alias:
  ls

my-clojure-alias
  clojure -M:dev
```

`do ^my-alias`

`do ^my-alias -lash`

`do ^my-clojure-alias:repl`

etc

### Home alias

By default the load in this order, with the last loaded taking priority.

1. Home aliases
2. Project aliases
3. Home aliases marked with `^override`
4. Project aliases marked with `^override`

When called with `do -me ^my-alias` it will reverse the priority of home vs project aliases

1. Project aliases
2. Home aliases
3. Project aliases marked with `^override`
4. Home aliases marked with `^override`

## Demo

I wrote a proof of concept in babashka.  It uses fixed files instead of looking for project / home files for demo purposes.

```sh
cd bb
```

### Project level task

```sh
bb do.bb --echo ^task1
=> clojure -M:project/1
```

Prefer home

```sh
bb do.bb --echo --me ^task1
=> clojure -M:home/1
```

Override from home

```sh
bb do.bb --echo ^task2
=> clojure -M:home/2
```

Override from project when preferring home

```sh
bb do.bb --echo -m ^task3
=> clojure -M:project/3
```

Override from home and project

```sh
bb do.bb --echo ^task4
=> clojure -M:project/4
```

Override from home and project, then favor home's override

```sh
bb do.bb --echo --me ^task4
=> clojure -M:home/4
```

### Home level task

```sh
bb do.bb --echo ^taskA
=> clojure -M:home/A
```

### Replace for params / aliases

```sh
bb do.bb --echo mycmd ^params1
=> mycmd --run home
```

```sh
bb do.bb --echo clojure -M:dev/run^alias1
=> clojure -M:dev/run:env/home
```

### Yes its just a string replacement tool

Add your own aliases and params

```sh
bb do.bb --echo ^task1:env/demo --run demo
=> clojure -M:project/1:env/demo --run demo
```

Combine aliases

```sh
bb do.bb --echo ^task1^alias1 ^params1
=> clojure -M:project/1:env/home --run home
```

Aliases of aliases

```sh
bb do.bb --echo ^task1^combined1
=> clojure -M:project/1:env/home --run home
```

Easy multiline.  Multiline tasks just get str joined.

```sh
bb do.bb --echo ^multiline1
=> mycmd  --param 1
```

If you want to run multiple tasks from this use &&

```sh
bb do.bb --echo ^multiline2
=> mycmd  && mycmd1
```

## Motivation

Im a lazy typer.

There are other cli tools that help alot.  Makefile, package.json, bashrc, etc.  None of these solutions work well for certain clojure specific aliases.  They don't include the same home -> project hierarchy you get from lein or deps.edn.

### Examples

#### bashrc

Lets looks at bashrc

```bash
alias lls=ls
```

You can call `lls -lash` like if you were calling `ls -lash`.  What you cant do is tie this to a project / repo.

```bash
alias ccm=clojure -M
```

You cant do  `ccm:my-repl`.

#### make file

```make
example:
  ls
```

This wont work `make example -lash` because the `-lash` is a param to `npm` not `ls`. Taking in params can be done with variables but are annoyingly specific to each call, and more verbose.

#### package.json

same as above

```json
  "scripts": {
    "lss": "ls",
  },
```

`npm run lss` is fine

`npm run lss -lash` doesn't work because the `-lash` is a param to `npm` not `ls`.

#### Clojure specific examples

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

# do.it

a purposely simple aliasing tool

## Motivation

Im a lazy typer.  deps.edn set up can lead to long commands that also need particular appending per environment.  Instead of infinite permutations of commands in something like makefile, let the user append in a simple way.

## do what?

Well everything and nothing.  Its just an aliasing tool, but can call any other build tool, etc.

Do.it does aliasing that is both based on an individual home alias config merged with a project specific alias config.

The idea here is to alias any bit of any cli call with basic string parsing so any command can be generated.

```do
my-alias:
  ls

my-clojure-alias
  clojure -M:dev
```

`do ^my-alias`

`do ^my-alias -lash`

`do ^my-clojure-alias:repl`

### Home / project hierarchy

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

## Use

### Install

Using homebrew

```sh
brew tap sirmspencer/tap
brew install doit
```

### Config

Create `do.it` (`.do.it` also supported) in your home or local / project directory.  Add a task.

```sh
mytask:
  ls -lash
```

## Limitations

- Not windows compatible
- Some path aliases need special treatment.  For example `~/` gets replaced with the full path from system properties.  Not sure how many others there are.
- The local / project file is only read from current directory

## Wishlist

- Read local from project root and not just current directory, if in a git project.
- Variables. sum like `^task:repl --param ^var1^=dev`

## Examples

Refer to /demo for the config used to generate these.

### Project level task

```sh
doit --echo ^task1
=> clojure -M:project/1
```

Prefer home

```sh
doit --echo --me ^task1
=> clojure -M:home/1
```

Override from home

```sh
doit --echo ^task2
=> clojure -M:home/2
```

Override from project when preferring home

```sh
doit --echo -m ^task3
=> clojure -M:project/3
```

Override from home and project

```sh
doit --echo ^task4
=> clojure -M:project/4
```

Override from home and project, then favor home's override

```sh
doit --echo --me ^task4
=> clojure -M:home/4
```

### Home level task

```sh
doit --echo ^taskA
=> clojure -M:home/A
```

### Replace for params / aliases

```sh
doit --echo mycmd ^params1
=> mycmd --run home
```

```sh
doit --echo clojure -M:dev/run^alias1
=> clojure -M:dev/run:env/home
```

### Yes its just a string replacement tool

Add your own aliases and params

```sh
doit --echo ^task1:env/demo --run demo
=> clojure -M:project/1:env/demo --run demo
```

Combine aliases

```sh
doit --echo ^task1^alias1 ^params1
=> clojure -M:project/1:env/home --run home
```

Aliases of aliases

```sh
doit --echo ^task1^combined1
=> clojure -M:project/1:env/home --run home
```

Easy multiline.  Multiline tasks just get str joined.

```sh
doit --echo ^multiline1
=> mycmd  --param 1
```

If you want to run multiple tasks from this use &&

```sh
doit --echo ^multiline2
=> mycmd  && mycmd1
```

## Why not other tools?

There are other cli tools that help alot.  Makefile, package.json, bashrc, etc.  None of these solutions work well for certain clojure specific aliases.  They don't include the same home -> project hierarchy you get from lein or deps.edn.

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

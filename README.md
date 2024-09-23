# do.it

a purposely simple aliasing tool

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

`do ^my-alias` => `ls`

`do ^my-alias -lash` => `ls -lash`

`do ^my-clojure-alias:repl` => `clojure -M:dev:repl`

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

## Motivation

Im a lazy typer.

### Hey I use clojure deps.edn

deps.edn set up can lead to long commands that also need particular appending per environment.  This is of course user preference on how the aliases are set up.  In my team we have many devs using many set ups.  If we were to use Makefile, for example, there would be a huge permutation of commands like `clojure -M:dev:vscode`, `clojure -M:dev:emacs`, `clojure -M:dev:test:vscode`, `clojure -M:dev:test:emacs`, etc depending on the task, environment, and dev preference.  Something like Makefile would need a long list of combinations to cover them all.

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
  ls
```

Call with

```sh
doit ^mytask -lash
```

### Other commands

Show help

```sh
doit --help
```

List the combined set of tasks

```sh
doit --ls
```

Echo result instead of shelling it

```sh
doit --echo
```

## Limitations

- Not windows compatible
- Some path aliases need special treatment.  For example `~/` gets replaced with the full path from system properties.  Not sure how many others there are.
- The local / project file is only read from current directory

## Wishlist

- Add tests
- Read local from project root and not just current directory, if in a git project.
- Variables. sum like `^task:repl --param ^var1^=dev`
- Windows compatible

## Examples

[see examples](./docs/examples.md)

## Why not other tools?

There are other cli tools that help alot.  Makefile, package.json, bashrc, etc.  None of these solutions work well for certain clojure specific aliases.  They don't include the same home -> project hierarchy you get from lein or deps.edn.

[some examples of others](./docs/the-other.md)

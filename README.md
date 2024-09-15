# do

a purposely simple aliasing tool

## do what? 

Well anything and nothing.  

A system to do aliasing that is both based on an idividual home alias config merged with a project specific alias config.

This will be an aliasing tool so simple it can do anything.  The idea here is to alias the first bit of any cli call and then also allow for any additional part of a cli call to be added.

```do
my-alias:
  ls
```

`do my-alias`

`do my alias -lash`

etc

## Motivation

There are other cli tools that help alot.  Makefile, package.json, bashrc, etc.  They all help but have limitations.  The limitations are about how much you can extend a call.

There is also clojure specific (or at least in my domain) calls that are hard.


## Examples

### bashrc

Lets looks at bashrc

```bash
alias lls=ls
```

You can call `lls -lash` like if you were calling `ls -lash`.  Great.  What you cant do is tie this to a project / repo. 

#### appending 

```bash
alias ccm=clojure -M
```

You cant do  `ccm:my-repl`

### make file

```
myexapmple
  ls
```

This wont work `make ls -lash`. taking in params are annoyingling specific to each call.

### package.json

same as above

```json
  "scripts": {
    "lss": "ls",
  },
```

`npm run lss` is fine

`npm run lss -lash` doesnt work because the `-lash` is an input to `npm` not `ls`.

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

## Plan

create a tool that can take any cli call and replace the first bit with an alias.  This is a basic string replacement really, but with the same hierarchy youd expect from home / repo.  The alias definitions can be per project / repo specific calls. That sorta exists, but in this case the local dev can add closure aliases or cli params.

For example 

```do
m-that-clojure
   clojure -M

test:
   lein kaocha
```

`m-that-clojure:dev` -> `clojure -M:dev`

`do test --wactch` -> `lain kaocha --watch`

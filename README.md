# do

a purposely simple aliasing tool

## do what? 

Well anything and nothing.  

This is an aliasing tool so simple it can do anything.  The idea here is to alias the first bit of any cli call and then also allow for any additional part of a cli call to be added.

## Motivation

There are other cli tools that help alot.  Makefile, package.json, bashrc, etc.  They all help but have limitations.  The limitations are about how much you can extend a call.

There is also clojure specific (or at least in my domain) calls that are hard.


## Examples

### bashrc

Lets looks at bashrc

```bash
alias lls=ls
```

You can call `lls -lash` like if you were calling `ls -lash`.  Great.  What you cant do is tie this to a project / repo.  Doesnt solve clojure specific syntax like `lls:my-thing`

### make file

```
myexapmple
  ls
```

This wont work `make ls -lash`. taking in params are annoyingling specific to each call.

### package.json



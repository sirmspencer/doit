# Examples

Refer to /demo for the config used to generate these.

## Project level task

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

## Home level task

```sh
doit --echo ^taskA
=> clojure -M:home/A
```

## Replace for params / aliases

```sh
doit --echo mycmd ^params1
=> mycmd --run home
```

```sh
doit --echo clojure -M:dev/run^alias1
=> clojure -M:dev/run:env/home
```

## Yes its just a string replacement tool

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

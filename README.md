# digraph-dependency-maven-plugin
Digraph generator for package dependencies within a project

Build Plugin to create a [DOT](https://en.wikipedia.org/wiki/DOT_(graph_description_language))
format diagram showing the dependencies between the packages in a project.

It works by scanning the source code and processing import statements.

Java parsing is handled using the [JavaParser](https://github.com/javaparser/javaparser)
project.

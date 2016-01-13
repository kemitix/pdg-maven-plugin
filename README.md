# digraph-dependency-maven-plugin
Digraph generator for package dependencies within a project

Build Plugin to create a [DOT](https://en.wikipedia.org/wiki/DOT_(graph_description_language))
format diagram showing the dependencies between the packages in a project.

It works by scanning the source code and processing import statements.

Java parsing is handled using the [JavaParser](https://github.com/javaparser/javaparser)
project.

## Usage

Add the following build plugin to your pom.xml:

            <plugin>
                <groupId>net.kemitix</groupId>
                <artifactId>digraph-dependency-maven-plugin</artifactId>
                <version>${digraph.version}</version>
                <configuration>
                    <basePackage>com.example</basePackage>
                    <!-- <includeTests>false</includeTests> -->
                </configuration>
            </plugin>

The `basePackage` is required and `includeTests` is optional, defaulting to not including them.


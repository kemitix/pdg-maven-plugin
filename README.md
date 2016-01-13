# digraph-dependency-maven-plugin
Digraph generator for package dependencies within a project

Build Plugin to create a [DOT](https://en.wikipedia.org/wiki/DOT_(graph_description_language))
format diagram showing the dependencies between the packages in a project.

It works by scanning the source code and processing import statements.

Java parsing is handled using the [JavaParser](https://github.com/javaparser/javaparser)
project.

## Usage

Add the following to you pom.xml:

    <build>
        <plugins>
            <plugin>
                <groupId>net.kemitix</groupId>
                <artifactId>digraph-dependency-maven-plugin</artifactId>
                <version>0.1.0</version>
                <configuration>
                    <basePackage>com.example</basePackage>
                    <!-- <includeTests>false</includeTests> -->
                    <!-- <debug>true</debug> -->
                </configuration>
            </plugin>
        </plugins>
    </build>

The `basePackage` is required while `includeTests` and `debug` are optional. Defaults are as shown.


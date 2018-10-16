/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Paul Campbell
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.kemitix.pdg.maven.scan;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.PackageDeclaration;
import net.kemitix.pdg.maven.DependencyData;
import net.kemitix.pdg.maven.DigraphMojo;

import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Analyses a Java source file for package and import statements.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@Immutable
class DefaultSourceFileAnalyser implements SourceFileAnalyser {

    private static final Pattern METHOD_IMPORT = Pattern.compile("^(?<package>.+)\\.(?<class>.+)\\.(?<method>.+)");

    private static final Pattern CLASS_IMPORT = Pattern.compile("^(?<package>.+)\\.(?<class>.+)");

    private final DigraphMojo mojo;

    /**
     * Constructor.
     *
     * @param mojo The Maven Mojo
     */
    @Inject
    DefaultSourceFileAnalyser(final DigraphMojo mojo) {
        this.mojo = mojo;
    }

    @Override
    public void analyse(
            final DependencyData dependencyData, final InputStream inputStream
                       ) {
        try {
            CompilationUnit cu = JavaParser.parse(inputStream);
            cu.getPackageDeclaration()
              .ifPresent(pd -> analyseUnit(pd, cu, dependencyData));
        } catch (ParseProblemException ex) {
            mojo.getLog()
                .error("Error parsing file " + inputStream, ex);
        }
    }

    @SuppressWarnings("npathcomplexity")
    private void analyseUnit(
            final PackageDeclaration aPackage, final CompilationUnit cu, final DependencyData dependencyData
                            ) {
        String packageName = aPackage.getName()
                                     .toString();
        cu.getImports()
          .forEach((ImportDeclaration id) -> {
              final String name = id.getName()
                                    .toString();
              Matcher m;
              if (id.isStatic() && !id.isAsterisk()) {
                  m = METHOD_IMPORT.matcher(name);
              } else {
                  m = CLASS_IMPORT.matcher(name);
              }
              if (m.find()) {
                  dependencyData.addDependency(packageName, m.group("package"));
              }
          });
    }
}

package net.kemitix.dependency.digraph.maven.plugin;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.PackageDeclaration;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

/**
 * Analyses a Java source file for package and import statements.
 *
 * @author pcampbell
 */
class DefaultSourceFileAnalyser extends AbstractMojoService
        implements SourceFileAnalyser {

    @Inject
    private DependencyData dependencyData;

    private static final Pattern METHOD_IMPORT
            = Pattern.compile("^(?<package>.+)\\.(?<class>.+)\\.(?<method>.+)");

    private static final Pattern CLASS_IMPORT
            = Pattern.compile("^(?<package>.+)\\.(?<class>.+)");

    @Override
    public void analyse(final InputStream inputStream) {
        try {
            CompilationUnit cu = JavaParser.parse(inputStream);
            final PackageDeclaration aPackage = cu.getPackage();
            if (aPackage != null) {
                String packageName = aPackage.getName().toString();
                cu.getImports().forEach((ImportDeclaration id) -> {
                    final String name = id.getName().toString();
                    Matcher m;
                    if (id.isStatic() && !id.isAsterisk()) {
                        m = METHOD_IMPORT.matcher(name);
                    } else {
                        m = CLASS_IMPORT.matcher(name);
                    }
                    if (m.find()) {
                        dependencyData.addDependency(
                                packageName, m.group("package"));
                    }
                });
            }
        } catch (ParseException ex) {
            getLog().error("Error parsing file " + inputStream, ex);
        }

    }
}

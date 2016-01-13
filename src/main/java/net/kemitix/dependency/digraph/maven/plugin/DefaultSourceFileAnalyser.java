package net.kemitix.dependency.digraph.maven.plugin;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;

import javax.inject.Inject;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public void analyse(final File file) {
        getMojo().getLog().info("analysing: " + file);
        try {
            CompilationUnit cu = JavaParser.parse(file);
            // the package being parsed
            String packageName = cu.getPackage().getName().toString();
            // identify classes being imported
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
        } catch (ParseException ex) {
            getLog().error("Error parsing file " + file, ex);
        } catch (IOException ex) {
            getLog().error("Error reading file " + file, ex);
        }

    }
}

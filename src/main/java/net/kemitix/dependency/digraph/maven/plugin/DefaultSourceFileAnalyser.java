package net.kemitix.dependency.digraph.maven.plugin;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

import javax.inject.Inject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Analyses a Java source file for package and import statements.
 *
 * @author pcampbell
 */
public class DefaultSourceFileAnalyser extends AbstractMojoService
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

            // the class being parsed
            String packageName = cu.getPackage().getName().toString();
            final List<TypeDeclaration> types = cu.getTypes();
            // types is null for package-info.java files
            if (types != null && types.size() > 0) {
                final TypeDeclaration item0 = types.get(0);
                String className = item0.getName();

                // identify classes being imported
                final List<ImportDeclaration> imports = cu.getImports();
                if (imports != null) {
                    imports.forEach((ImportDeclaration id) -> {
                        final String name = id.getName().toString();
                        Matcher m;
                        if (id.isStatic() && !id.isAsterisk()) {
                            m = METHOD_IMPORT.matcher(name);
                        } else {
                            m = CLASS_IMPORT.matcher(name);
                        }
                        if (m.find()) {
                            dependencyData.addDependency(packageName, className,
                                    m.group("package"), m.group("class"));
                        }
                    });
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(DefaultSourceFileAnalyser.class.getName())
                    .log(Level.SEVERE, "Error parsing file " + file, ex);
        } catch (IOException ex) {
            Logger.getLogger(DefaultSourceFileAnalyser.class.getName())
                    .log(Level.SEVERE, "Error reading file " + file, ex);
        }

    }
}

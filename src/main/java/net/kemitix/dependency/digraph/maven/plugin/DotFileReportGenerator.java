package net.kemitix.dependency.digraph.maven.plugin;

import lombok.NonNull;

import javax.inject.Inject;

/**
 * Generates a dot file as dependency report generator.
 *
 * @author pcampbell
 */
public class DotFileReportGenerator extends AbstractMojoService
        implements ReportGenerator {

    @Inject
    private DependencyData dependencyData;

    @Override
    public String generate(@NonNull final String basePackage) {
        StringBuilder report = new StringBuilder();
        report.append("digraph {\n");
        report.append("\tnode[shape=\"box\"];\n");
        dependencyData.getUserPackages().forEach((String user) -> {
            if (withinBasePackage(basePackage, user)) {
                dependencyData.getUsedPackages(user).forEach((String used) -> {
                    if (withinBasePackage(basePackage, used)) {
                        report.append("\t\"")
                                .append(strip(basePackage, user))
                                .append("\" -> \"")
                                .append(strip(basePackage, used))
                                .append("\";\n");
                    }
                });
            }
        });
        report.append("}");
        return report.toString();
    }

    private boolean withinBasePackage(
            @NonNull final String basePackage,
            @NonNull final String thePackage) {
        return basePackage.equals(thePackage)
                || thePackage.startsWith(basePackage + ".");
    }

    private String strip(
            final String basePackage,
            final String packageName) {
        if (basePackage.equals(packageName)) {
            return "[base]";
        }
        return packageName.replace(basePackage, "..");
    }
}

package net.kemitix.dependency.digraph.maven.plugin;

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
    public String generate() {
        StringBuilder report = new StringBuilder();
        report.append("digraph {\n");
        dependencyData.getUserPackages().forEach((String user) -> {
            dependencyData.getUsedPackages(user).forEach((String used) -> {
                report.append("\t\"").append(user).append("\" -> \"")
                        .append(used).append("\";\n");
            });
        });
        report.append("}");
        return report.toString();
    }

}

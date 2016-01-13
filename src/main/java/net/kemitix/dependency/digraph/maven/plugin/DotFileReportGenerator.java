package net.kemitix.dependency.digraph.maven.plugin;

import javax.inject.Inject;

/**
 * Generates a dot file as dependency report generator.
 *
 * @author pcampbell
 */
class DotFileReportGenerator extends AbstractMojoService
        implements ReportGenerator {

    private final NodePathGenerator nodePathGenerator;

    /**
     * Injected constructor.
     *
     * @param nodeIdGenerator the node id generator
     */
    @Inject
    DotFileReportGenerator(final NodePathGenerator nodeIdGenerator) {
        this.nodePathGenerator = nodeIdGenerator;
    }

    @Override
    public String generate(final DotFileFormat dotFileFormat) {
        final StringBuilder report = new StringBuilder();
        report.append(dotFileFormat.renderReport());
        return report.toString();
    }

}

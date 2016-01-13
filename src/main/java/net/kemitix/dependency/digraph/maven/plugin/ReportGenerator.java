package net.kemitix.dependency.digraph.maven.plugin;

/**
 * Interface for creating dependency data report.
 *
 * @author pcampbell
 */
interface ReportGenerator extends MojoService {

    /**
     * Generates the dependency report.
     *
     * @param dotFileFormat the format specific report generator
     *
     * @return the report
     */
    String generate(DotFileFormat dotFileFormat);

}

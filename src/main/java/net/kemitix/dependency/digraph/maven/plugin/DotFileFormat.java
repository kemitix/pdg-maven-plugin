package net.kemitix.dependency.digraph.maven.plugin;

/**
 * Interface for report generators in a specific format.
 *
 * @author pcampbell
 */
interface DotFileFormat {

    /**
     * Generates the report.
     *
     * @return the formatted report
     */
    String renderReport();

}

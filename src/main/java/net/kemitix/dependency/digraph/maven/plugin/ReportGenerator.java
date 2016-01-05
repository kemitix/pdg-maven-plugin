package net.kemitix.dependency.digraph.maven.plugin;

/**
 * Interface for creating dependency data report.
 *
 * @author pcampbell
 */
public interface ReportGenerator extends MojoService {

    /**
     * Generates the dependency report.
     *
     * @return the report
     */
    String generate();

}

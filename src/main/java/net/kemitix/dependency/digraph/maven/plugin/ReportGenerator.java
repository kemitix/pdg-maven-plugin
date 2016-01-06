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
     * @param basePackage only report dependencies within this package
     *
     * @return the report
     */
    String generate(String basePackage);

}

package net.kemitix.dependency.digraph.maven.plugin;

import net.kemitix.node.Node;

/**
 * Interface for creating dependency data report.
 *
 * @author pcampbell
 */
interface ReportGenerator extends MojoService {

    /**
     * Generates the dependency report.
     *
     * @param baseNode the node to report on
     *
     * @return the report
     */
    String generate(Node<PackageData> baseNode);

}

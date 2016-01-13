package net.kemitix.dependency.digraph.maven.plugin;

import net.kemitix.node.Node;

/**
 * Interface for creating node paths.
 *
 * @author Paul Campbell
 */
interface NodePathGenerator {

    /**
     * Returns the delimited path of the node from the base.
     *
     * @param node      the node to generate the path for
     * @param base      the base of the path
     * @param delimiter the delimiter
     *
     * @return the path of the node from the base
     */
    String getPath(Node<PackageData> node, Node<PackageData> base,
            String delimiter);

}

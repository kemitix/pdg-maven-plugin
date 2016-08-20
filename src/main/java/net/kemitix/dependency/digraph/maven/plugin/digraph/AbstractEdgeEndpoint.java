package net.kemitix.dependency.digraph.maven.plugin.digraph;

import net.kemitix.dependency.digraph.maven.plugin.DotFileFormat;
import net.kemitix.dependency.digraph.maven.plugin.NodeHelper;
import net.kemitix.dependency.digraph.maven.plugin.PackageData;
import net.kemitix.node.Node;

/**
 * .
 *
 * @author pcampbell
 */
abstract class AbstractEdgeEndpoint extends AbstractGraphElement
        implements EdgeEndpoint {

    private final Node<PackageData> packageDataNode;

    AbstractEdgeEndpoint(
            final DotFileFormat dotFileFormat,
            final Node<PackageData> packageDataNode) {
        super(dotFileFormat);
        this.packageDataNode = packageDataNode;
    }

    /**
     * Creates a copy of the package data node and returns it.
     *
     * @return a copy of the package data node
     */
    public Node<PackageData> getPackageDataNode() {
        return NodeHelper.copyOf(packageDataNode);
    }
}

package net.kemitix.dependency.digraph.maven.plugin.digraph;

import net.kemitix.dependency.digraph.maven.plugin.PackageData;
import net.kemitix.node.Node;

/**
 * Represents and object that contains a {@link net.kemitix.node.Node}
 * containing a {@link net.kemitix.dependency.digraph.maven.plugin.PackageData}.
 */
public interface HasPackageDataNode {

    /**
     * Sets the package data node.
     *
     * @param packageDataNode the package data node
     */
    void setPackageDataNode(Node<PackageData> packageDataNode);

    /**
     * Returns the package data node.
     *
     * @return the package data node
     */
    Node<PackageData> getPackageDataNode();

}

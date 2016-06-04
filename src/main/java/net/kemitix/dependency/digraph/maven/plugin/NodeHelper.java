package net.kemitix.dependency.digraph.maven.plugin;

import net.kemitix.node.Node;

/**
 * Helpers for Node.
 *
 * @author pcampbell
 */
final class NodeHelper {

    private NodeHelper() {
    }

    static PackageData getRequiredData(
            final Node<PackageData> node) {
        return node.getData()
                   .orElseThrow(() -> new IllegalStateException(
                           "Node has no package data"));
    }

}

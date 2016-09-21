package net.kemitix.dependency.digraph.maven.plugin;

import lombok.val;

import javax.annotation.concurrent.Immutable;

import net.kemitix.node.Node;
import net.kemitix.node.Nodes;

/**
 * Helpers for Node.
 *
 * @author pcampbell
 */
@Immutable
public final class NodeHelper {

    private NodeHelper() {
    }

    static PackageData getRequiredData(
            final Node<PackageData> node) {
        return node.getData()
                   .orElseThrow(() -> new IllegalStateException(
                           "Node has no package data"));
    }

    /**
     * Creates a copy of the package data node and returns it.
     *
     * @param source the node to copy
     *
     * @return a copy of the package data node
     */
    public static Node<PackageData> copyOf(final Node<PackageData> source) {
        val optionalData = source.getData();
        PackageData data = null;
        if (optionalData.isPresent()) {
            data = optionalData.get();
        }
        val optionalParent = source.getParent();
        Node<PackageData> parent = null;
        if (optionalParent.isPresent()) {
            parent = optionalParent.get();
        }
        if (parent != null) {
            return Nodes.namedChild(data, source.getName(), parent);
        }
        return Nodes.namedRoot(data, source.getName());
    }

}

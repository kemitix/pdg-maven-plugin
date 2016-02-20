package net.kemitix.dependency.digraph.maven.plugin;

import net.kemitix.node.Node;

/**
 * Default implementation of the Node ID Generator.
 *
 * @author Paul Campbell
 */
class DefaultNodePathGenerator implements NodePathGenerator {

    @Override
    public String getPath(
            final Node<PackageData> node, final Node<PackageData> base,
            final String delimiter) {
        final Node<PackageData> parent = node.getParent();
        // if node has no parent, then "" is the path
        if (parent == null) {
            return "";
        }
        // if node's parent is base, then node is the path
        if (parent.equals(base)) {
            return node.getData().getName();
        }
        // else append to parent path
        final StringBuilder path = new StringBuilder();
        return path.append(getPath(parent, base, delimiter))
                   .append(delimiter)
                   .append(node.getData().getName())
                   .toString();
    }

}

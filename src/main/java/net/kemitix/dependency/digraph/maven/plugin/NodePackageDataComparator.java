package net.kemitix.dependency.digraph.maven.plugin;

import net.kemitix.node.Node;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Comparator for sorting {@link PackageData} {@link Node}s.
 */
@SuppressWarnings("serial")
class NodePackageDataComparator
        implements Comparator<Node<PackageData>>, Serializable {

    NodePackageDataComparator() {
    }

    @Override
    public int compare(
            final Node<PackageData> o1,
            final Node<PackageData> o2) {
        return o1.getData().getName()
                .compareTo(o2.getData().getName());
    }
}

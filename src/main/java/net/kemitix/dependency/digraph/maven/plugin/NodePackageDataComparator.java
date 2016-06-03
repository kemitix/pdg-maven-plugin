package net.kemitix.dependency.digraph.maven.plugin;

import lombok.val;

import java.io.Serializable;
import java.util.Comparator;

import net.kemitix.node.Node;

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
            final Node<PackageData> o1, final Node<PackageData> o2) {
        val pd1 = o1.getData();
        val pd2 = o2.getData();
        if (pd1.isPresent() && pd2.isPresent()) {
            val n1 = pd1.get().getName();
            val n2 = pd2.get().getName();
            return n1.compareTo(n2);
        }
        throw new IllegalStateException("Node has no package data");
    }
}

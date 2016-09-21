package net.kemitix.dependency.digraph.maven.plugin;

import lombok.val;

import javax.annotation.concurrent.Immutable;

import net.kemitix.node.Node;

/**
 * Default implementation of the Node ID Generator.
 *
 * @author Paul Campbell
 */
@Immutable
class DefaultNodePathGenerator implements NodePathGenerator {

    @Override
    public String getPath(
            final Node<PackageData> node, final Node<PackageData> base,
            final String delimiter) {
        val path = new StringBuilder();
        node.getData()
            .map(PackageData::getName)
            .ifPresent(name -> node.getParent().ifPresent(parent -> {
                if (!parent.equals(base)) {
                    path.append(getPath(parent, base, delimiter))
                        .append(delimiter);
                }
                path.append(name);
            }));
        return path.toString();
    }

}

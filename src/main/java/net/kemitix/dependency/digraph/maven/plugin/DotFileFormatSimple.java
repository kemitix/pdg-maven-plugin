package net.kemitix.dependency.digraph.maven.plugin;

import java.util.stream.Collectors;

import net.kemitix.dependency.digraph.maven.plugin.digraph.EdgeElement;
import net.kemitix.dependency.digraph.maven.plugin.digraph.NodeElement;
import net.kemitix.dependency.digraph.maven.plugin.digraph.Subgraph;
import net.kemitix.node.Node;

/**
 * Generates a dot file dependency report as a simple digraph.
 *
 * @author pcampbell
 */
class DotFileFormatSimple extends AbstractDotFileFormat {

    DotFileFormatSimple(
            final Node<PackageData> base,
            final NodePathGenerator nodePathGenerator) {
        super(base, nodePathGenerator);
    }

    @Override
    String render(
            final Subgraph subgraph) {
        final String id = subgraph.getId();
        String node = "";
        if (!id.startsWith("_")) {
            node = quoted(id) + "\n";
        }
        return node + subgraph.getElements()
                              .stream()
                              .map(this::render)
                              .collect(Collectors.joining("\n"));
    }

    @Override
    String render(
            final EdgeElement edgeElement) {
        return quoted(edgeElement.getTail().getId()) + " -> " + quoted(
                edgeElement.getHead().getId());
    }

    @Override
    String render(
            final NodeElement nodeElement) {
        return quoted(nodeElement.getId());
    }

}

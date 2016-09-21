package net.kemitix.dependency.digraph.maven.plugin;

import java.util.stream.Collectors;

import javax.annotation.concurrent.Immutable;

import net.kemitix.dependency.digraph.maven.plugin.digraph.EdgeElement;
import net.kemitix.dependency.digraph.maven.plugin.digraph.GraphElement;
import net.kemitix.dependency.digraph.maven.plugin.digraph.NodeElement;
import net.kemitix.dependency.digraph.maven.plugin.digraph.Subgraph;
import net.kemitix.node.Node;

/**
 * Generates a dot file dependency report as a simple digraph.
 *
 * @author pcampbell
 */
@Immutable
class DotFileFormatSimple extends AbstractDotFileFormat {

    public static final String NEWLINE = "\n";

    DotFileFormatSimple(
            final Node<PackageData> base,
            final NodePathGenerator nodePathGenerator) {
        super(base, nodePathGenerator);
    }

    @Override
    public String render(final Subgraph subgraph) {
        final String id = subgraph.getId();
        String node = "";
        if (!id.startsWith("_")) {
            node = quoted(id) + NEWLINE;
        }
        return node + subgraph.getElements()
                              .stream()
                              .map(GraphElement::render)
                              .collect(Collectors.joining(NEWLINE));
    }

    @Override
    public String render(final NodeElement nodeElement) {
        return quoted(nodeElement.getId());
    }

    @Override
    public String render(final EdgeElement edgeElement) {
        return quoted(edgeElement.getTail().getId()) + " -> " + quoted(
                edgeElement.getHead().getId());
    }

}

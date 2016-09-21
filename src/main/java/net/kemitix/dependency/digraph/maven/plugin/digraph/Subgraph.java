package net.kemitix.dependency.digraph.maven.plugin.digraph;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.concurrent.Immutable;

import net.kemitix.dependency.digraph.maven.plugin.DotFileFormat;
import net.kemitix.dependency.digraph.maven.plugin.PackageData;
import net.kemitix.node.Node;

/**
 * Represets a subgraph/cluster.
 *
 * @author pcampbell
 */
@Getter
@Immutable
public class Subgraph extends AbstractGraphElement
        implements ElementContainer, HasId, HasLabel, EdgeEndpoint {

    private final List<GraphElement> elements = new ArrayList<>();

    private final String id;

    private final String label;

    private final Node<PackageData> packageDataNode;

    /**
     * Constructor.
     *
     * @param packageDataNode the package data node for this subgraph
     * @param id              the id of the subgraph
     * @param label           the label of the subgraph
     * @param dotFileFormat   the output format
     */
    public Subgraph(
            final Node<PackageData> packageDataNode, final String id,
            final String label, final DotFileFormat dotFileFormat) {
        super(dotFileFormat);
        this.packageDataNode = packageDataNode;
        this.id = id;
        this.label = label;
    }

    @Override
    public final boolean add(final GraphElement graphElement) {
        return elements.add(graphElement);
    }

    @Override
    public final String render() {
        return getDotFileFormat().render(this);
    }
}

package net.kemitix.dependency.digraph.maven.plugin.digraph;

import lombok.Getter;

import net.kemitix.dependency.digraph.maven.plugin.DotFileFormat;
import net.kemitix.dependency.digraph.maven.plugin.PackageData;
import net.kemitix.node.Node;

/**
 * Represents a node on the graph.
 */
@Getter
public final class NodeElement extends AbstractEdgeEndpoint
        implements HasId, HasLabel {

    private final String id;

    private final String label;

    /**
     * Constructor.
     *
     * @param packageDataNode the package data node for this node element
     * @param id              the id of the node
     * @param label           the label of the node
     * @param dotFileFormat   the output format
     */
    public NodeElement(
            final Node<PackageData> packageDataNode, final String id,
            final String label, final DotFileFormat dotFileFormat) {
        super(dotFileFormat, packageDataNode);
        this.id = id;
        this.label = label;
    }

    @Override
    public String render() {
        return getDotFileFormat().render(this);
    }

}

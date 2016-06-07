package net.kemitix.dependency.digraph.maven.plugin.digraph;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import net.kemitix.dependency.digraph.maven.plugin.PackageData;
import net.kemitix.node.Node;

/**
 * Represets a subgraph/cluster.
 *
 * @author pcampbell
 */
@Setter
@Getter
public class Subgraph extends GraphElement
        implements ElementContainer, HasId, HasLabel, EdgeEndpoint {

    private final List<GraphElement> elements = new ArrayList<>();

    private String id;

    private String label;

    private Node<PackageData> packageDataNode;

    /**
     * Constructor.
     *
     * @param packageDataNode the package data node for this subgraph
     * @param id              the id of the subgraph
     * @param label           the label of the subgraph
     */
    public Subgraph(
            final Node<PackageData> packageDataNode, final String id,
            final String label) {
        setPackageDataNode(packageDataNode);
        setId(id);
        setLabel(label);
    }

    @Override
    public boolean add(
            final GraphElement graphElement) {
        return elements.add(graphElement);
    }

}

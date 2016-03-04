package net.kemitix.dependency.digraph.maven.plugin;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import net.kemitix.dependency.digraph.maven.plugin.digraph.Digraph;
import net.kemitix.dependency.digraph.maven.plugin.digraph.EdgeElement;
import net.kemitix.dependency.digraph.maven.plugin.digraph.EdgeEndpoint;
import net.kemitix.dependency.digraph.maven.plugin.digraph.ElementContainer;
import net.kemitix.dependency.digraph.maven.plugin.digraph.GraphElement;
import net.kemitix.dependency.digraph.maven.plugin.digraph.NodeElement;
import net.kemitix.dependency.digraph.maven.plugin.digraph.NodeProperties;
import net.kemitix.dependency.digraph.maven.plugin.digraph.PropertyElement;
import net.kemitix.dependency.digraph.maven.plugin.digraph.Subgraph;
import net.kemitix.node.Node;

/**
 * Abstract base for {@link DotFileFormat} implementations.
 *
 * @author pcampbell
 */
public abstract class AbstractDotFileFormat implements DotFileFormat {

    @Getter(AccessLevel.PROTECTED)
    private final Node<PackageData> base;

    private final NodePathGenerator nodePathGenerator;

    private final NodePackageDataComparator nodePackageDataComparator
            = new NodePackageDataComparator();

    /**
     * Constructor.
     *
     * @param base              the base package
     * @param nodePathGenerator the node path generator
     */
    public AbstractDotFileFormat(
            final Node<PackageData> base,
            final NodePathGenerator nodePathGenerator) {
        this.base = base;
        this.nodePathGenerator = nodePathGenerator;
    }

    protected String getClusterId(final Node<PackageData> node) {
        return getPath(node, "_");
    }

    protected String quoted(final String text) {
        return "\"" + text + "\"";
    }

    protected String getPath(
            final Node<PackageData> headNode, final String delimiter) {
        return nodePathGenerator.getPath(headNode, getBase(), delimiter);
    }

    protected Digraph createDigraph() {
        Digraph digraph = new Digraph();
        digraph.add(new PropertyElement("compound", "true"));
        final NodeProperties nodeProperties = new NodeProperties();
        nodeProperties.add(new PropertyElement("shape", "box"));
        digraph.add(nodeProperties);
        return digraph;
    }

    @Override
    public String renderReport() {
        Digraph digraph = createDigraph();
        getNodeInjector().injectNodes(digraph, base);
        getUsageInjector().injectUsages(digraph, base);
        return render(digraph);
    }

    protected GraphUsageInjector getUsageInjector() {
        return (container, node) -> node.getChildren()
                                        .stream()
                                        .sorted(nodePackageDataComparator)
                                        .forEach(injectUsagesByChildren(
                                                container));
    }

    private Consumer<Node<PackageData>> injectUsagesByChildren(
            final ElementContainer container) {
        return (Node<PackageData> childNode) -> {
            childNode.getData()
                     .getUses()
                     .stream()
                     .filter(n -> n.isChildOf(getBase()))
                     .sorted(nodePackageDataComparator)
                     .map(usedNode -> createEdgeElement(childNode, usedNode))
                     .forEach(container::add);
            getUsageInjector().injectUsages(container, childNode);
        };
    }

    EdgeEndpoint findEdgeEndpoint(final Node<PackageData> node) {
        if (node.getChildren().isEmpty()) {
            return findNodeElement(node);
        }
        return findSubgraph(node);
    }

    NodeElement createNodeElement(
            final Node<PackageData> packageDataNode) {
        return new NodeElement(packageDataNode, getNodeId(packageDataNode),
                packageDataNode.getData().getName());
    }

    EdgeElement createEdgeElement(
            final Node<PackageData> tail, final Node<PackageData> head) {
        return new EdgeElement(findEdgeEndpoint(tail), findEdgeEndpoint(head));
    }

    protected NodeElement findNodeElement(final Node<PackageData> node) {
        if (!graphElements.containsKey(node)) {
            graphElements.put(node, createNodeElement(node));
        }
        return (NodeElement) graphElements.get(node);
    }

    Subgraph findSubgraph(final Node<PackageData> node) {
        if (!graphElements.containsKey(node)) {
            graphElements.put(node, createSubgraph(node));
        }
        return (Subgraph) graphElements.get(node);
    }

    private Subgraph createSubgraph(final Node<PackageData> node) {
        return new Subgraph(node, getClusterId(node), node.getData().getName());
    }

    private Map<Node<PackageData>, GraphElement> graphElements
            = new HashMap<>();

    protected GraphNodeInjector getNodeInjector() {
        return new GraphNodeInjector() {
            @Override
            public void injectNodes(
                    final ElementContainer container,
                    final Node<PackageData> node) {
                final Set<Node<PackageData>> children = node.getChildren();
                if (children.isEmpty()) {
                    container.add(findNodeElement(node));
                } else {
                    Subgraph subgraph = findSubgraph(node);
                    children.stream()
                            .sorted(nodePackageDataComparator)
                            .forEach(c -> this.injectNodes(subgraph, c));
                    container.add(subgraph);
                }
            }
        };
    }

    protected String getNodeId(final Node<PackageData> node) {
        return getPath(node, ".");
    }

    /**
     * Functional Interface for rendering a node.
     */
    @FunctionalInterface
    protected interface GraphNodeInjector {

        void injectNodes(
                final ElementContainer container, final Node<PackageData> node);

    }

    /**
     * Functional Interface for rendering a usage.
     */
    @FunctionalInterface
    protected interface GraphUsageInjector {

        void injectUsages(
                final ElementContainer container, final Node<PackageData> node);

    }

    String renderElements(final Collection<GraphElement> elements) {
        return elements.stream()
                       .map(this::render)
                       .collect(Collectors.joining("\n"));
    }

    String renderProperties(final Set<PropertyElement> properties) {
        return properties.stream()
                         .map(this::render)
                         .collect(Collectors.joining(";\n"));
    }

    String render(final GraphElement graphElement) {
        if (graphElement instanceof Digraph) {
            return render((Digraph) graphElement);
        }
        if (graphElement instanceof Subgraph) {
            return render((Subgraph) graphElement);
        }
        if (graphElement instanceof NodeProperties) {
            return render((NodeProperties) graphElement);
        }
        if (graphElement instanceof NodeElement) {
            return render((NodeElement) graphElement);
        }
        if (graphElement instanceof EdgeElement) {
            return render((EdgeElement) graphElement);
        }
        if (graphElement instanceof PropertyElement) {
            return render((PropertyElement) graphElement);
        }
        return "(graph-element)";
    }

    String render(final Digraph digraph) {
        return "digraph{\n" + renderElements(digraph.getElements()) + "}\n";
    }

    abstract String render(final Subgraph subgraph);

    String render(
            final NodeProperties nodeProperties) {
        return "node[" + renderProperties(nodeProperties.getProperties()) + "]";
    }

    String render(
            final NodeElement nodeElement) {
        final String id = nodeElement.getId();
        final String label = nodeElement.getLabel();
        if (id.equals(label)) {
            return quoted(id);
        } else {
            return quoted(id) + "[label=" + quoted(label) + "]";
        }
    }

    abstract String render(final EdgeElement edgeElement);

    String render(
            final PropertyElement propertyElement) {
        return propertyElement.getName() + "=" + quoted(
                propertyElement.getValue());
    }

}

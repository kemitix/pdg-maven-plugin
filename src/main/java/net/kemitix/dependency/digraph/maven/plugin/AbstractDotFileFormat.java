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

    public static final String CLOSE_BRACE = "]";

    public static final String DOUBLE_QUOTE = "\"";

    @Getter(AccessLevel.PROTECTED)
    private final Node<PackageData> base;

    private final NodePathGenerator nodePathGenerator;

    private final NodePackageDataComparator nodePackageDataComparator;

    private Map<Node<PackageData>, GraphElement> graphElements
            = new HashMap<>();

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
        this.nodePackageDataComparator = new NodePackageDataComparator();
        this.graphElements = new HashMap<>();
    }

    protected String getClusterId(final Node<PackageData> node) {
        return getPath(node, "_");
    }

    protected String getPath(
            final Node<PackageData> headNode, final String delimiter) {
        return nodePathGenerator.getPath(headNode, getBase(), delimiter);
    }

    protected Digraph createDigraph() {
        return new Digraph.Builder(this).build();
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
                     .ifPresent(data -> data.getUses()
                                            .stream()
                                            .filter(n -> n.isDescendantOf(
                                                    getBase()))
                                            .sorted(nodePackageDataComparator)
                                            .map(usedNode -> createEdgeElement(
                                                    childNode, usedNode))
                                            .forEach(container::add));
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
                NodeHelper.getRequiredData(packageDataNode).getName(), this);
    }

    EdgeElement createEdgeElement(
            final Node<PackageData> tail, final Node<PackageData> head) {
        return new EdgeElement(findEdgeEndpoint(tail), findEdgeEndpoint(head),
                this);
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
        return new Subgraph(node, getClusterId(node),
                NodeHelper.getRequiredData(node).getName(), this);
    }

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

    String render(final GraphElement graphElement) {
        String rendered = "(graph-element)";
        if (graphElement instanceof Digraph) {
            rendered = render((Digraph) graphElement);
        } else if (graphElement instanceof Subgraph) {
            rendered = render((Subgraph) graphElement);
        } else if (graphElement instanceof NodeProperties) {
            rendered = render((NodeProperties) graphElement);
        } else if (graphElement instanceof NodeElement) {
            rendered = render((NodeElement) graphElement);
        } else if (graphElement instanceof EdgeElement) {
            rendered = render((EdgeElement) graphElement);
        } else if (graphElement instanceof PropertyElement) {
            rendered = render((PropertyElement) graphElement);
        }
        return rendered;
    }

    String render(final Digraph digraph) {
        return "digraph{\n" + renderElements(digraph.getElements()) + "}\n";
    }

    abstract String render(final Subgraph subgraph);

    String render(
            final NodeProperties nodeProperties) {
        return "node[" + renderProperties(nodeProperties.getProperties())
                + CLOSE_BRACE;
    }

    String render(
            final NodeElement nodeElement) {
        final String id = nodeElement.getId();
        final String label = nodeElement.getLabel();
        if (id.equals(label)) {
            return quoted(id);
        } else {
            return quoted(id) + "[label=" + quoted(label) + CLOSE_BRACE;
        }
    }

    abstract String render(final EdgeElement edgeElement);

    String render(
            final PropertyElement propertyElement) {
        return propertyElement.getName() + "=" + quoted(
                propertyElement.getValue());
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

    protected String quoted(final String text) {
        return DOUBLE_QUOTE + text + DOUBLE_QUOTE;
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

}

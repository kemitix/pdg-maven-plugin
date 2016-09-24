/*
The MIT License (MIT)

Copyright (c) 2016 Paul Campbell

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package net.kemitix.dependency.digraph.maven.plugin;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.annotation.concurrent.Immutable;

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
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@Immutable
public abstract class AbstractDotFileFormat implements DotFileFormat {

    public static final String CLOSE_BRACE = "]";

    public static final String DOUBLE_QUOTE = "\"";

    @Getter(AccessLevel.PROTECTED)
    private final Node<PackageData> base;

    private final NodePathGenerator nodePathGenerator;

    private final NodePackageDataComparator nodePackageDataComparator;

    private final Map<Node<PackageData>, GraphElement> graphElements
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
    }

    @Override
    public final String renderReport() {
        Digraph digraph = createDigraph();
        getNodeInjector().injectNodes(digraph, base);
        getUsageInjector().injectUsages(digraph, base);
        return render(digraph);
    }

    @Override
    public final String render(final Digraph digraph) {
        return "digraph{\n" + renderElements(digraph.getElements()) + "}\n";
    }

    @Override
    public final String render(final NodeProperties nodeProperties) {
        return "node[" + renderProperties(nodeProperties.getProperties())
                + CLOSE_BRACE;
    }

    @Override
    public final String render(final PropertyElement propertyElement) {
        return propertyElement.getName() + "=" + quoted(
                propertyElement.getValue());
    }

    final String getClusterId(final Node<PackageData> node) {
        return getPath(node, "_");
    }

    final String getPath(
            final Node<PackageData> headNode, final String delimiter) {
        return nodePathGenerator.getPath(headNode, getBase(), delimiter);
    }

    final Digraph createDigraph() {
        return new Digraph.Builder(this).build();
    }

    final GraphUsageInjector getUsageInjector() {
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

    final EdgeEndpoint findEdgeEndpoint(final Node<PackageData> node) {
        if (node.getChildren().isEmpty()) {
            return findNodeElement(node);
        }
        return findSubgraph(node);
    }

    final NodeElement createNodeElement(
            final Node<PackageData> packageDataNode) {
        return new NodeElement(packageDataNode, getNodeId(packageDataNode),
                NodeHelper.getRequiredData(packageDataNode).getName(), this);
    }

    final EdgeElement createEdgeElement(
            final Node<PackageData> tail, final Node<PackageData> head) {
        return new EdgeElement(findEdgeEndpoint(tail), findEdgeEndpoint(head),
                this);
    }

    final NodeElement findNodeElement(final Node<PackageData> node) {
        if (!graphElements.containsKey(node)) {
            graphElements.put(node, createNodeElement(node));
        }
        return (NodeElement) graphElements.get(node);
    }

    final Subgraph findSubgraph(final Node<PackageData> node) {
        if (!graphElements.containsKey(node)) {
            graphElements.put(node, createSubgraph(node));
        }
        return (Subgraph) graphElements.get(node);
    }

    private Subgraph createSubgraph(final Node<PackageData> node) {
        return new Subgraph(node, getClusterId(node),
                NodeHelper.getRequiredData(node).getName(), this);
    }

    final GraphNodeInjector getNodeInjector() {
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

    final String getNodeId(final Node<PackageData> node) {
        return getPath(node, ".");
    }

    final String renderElements(final Collection<GraphElement> elements) {
        return elements.stream()
                       .map(GraphElement::render)
                       .collect(Collectors.joining("\n"));
    }

    final String renderProperties(final Set<PropertyElement> properties) {
        return properties.stream()
                         .map(GraphElement::render)
                         .collect(Collectors.joining(";\n"));
    }

    final String quoted(final String text) {
        return DOUBLE_QUOTE + text + DOUBLE_QUOTE;
    }

    /**
     * Functional Interface for rendering a node.
     */
    @FunctionalInterface
    interface GraphNodeInjector {

        void injectNodes(
                final ElementContainer container, final Node<PackageData> node);

    }

    /**
     * Functional Interface for rendering a usage.
     */
    @FunctionalInterface
    interface GraphUsageInjector {

        void injectUsages(
                final ElementContainer container, final Node<PackageData> node);

    }

}

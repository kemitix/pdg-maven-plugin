package net.kemitix.dependency.digraph.maven.plugin;

import lombok.AccessLevel;
import lombok.Getter;
import net.kemitix.node.Node;

/**
 * Abstract base for {@link DotFileFormat} implementations.
 *
 * @author pcampbell
 */
abstract class AbstractDotFileFormat implements DotFileFormat {

    @Getter(AccessLevel.PROTECTED)
    private final Node<PackageData> base;

    private final NodePathGenerator nodePathGenerator;

    /**
     * Constructor.
     *
     * @param base              the base package
     * @param nodePathGenerator the node path generator
     */
    AbstractDotFileFormat(
            final Node<PackageData> base,
            final NodePathGenerator nodePathGenerator) {
        this.base = base;
        this.nodePathGenerator = nodePathGenerator;
    }

    @Override
    public String renderReport() {
        final StringBuilder report = new StringBuilder();
        report.append(renderGraphStart());
        report.append(renderElements(this::renderNode, this::renderUsages));
        report.append(renderGraphFinish());
        return report.toString();
    }

    protected String getPath(
            final Node<PackageData> headNode,
            final String delimiter) {
        return nodePathGenerator.getPath(headNode, getBase(), delimiter);
    }

    protected String renderElements(
            final NodeRenderer nodeRenderer,
            final UsageRenderer usageRenderer) {
        final StringBuilder render = new StringBuilder();
        render.append(nodeRenderer.renderNode(base));
        render.append(usageRenderer.renderUsage(base));
        return render.toString();
    }

    String renderGraphStart() {
        return "digraph{compound=true;node[shape=box]\n";
    }

    abstract String renderNode(final Node<PackageData> node);

    abstract String renderUsages(final Node<PackageData> node);

    String renderGraphFinish() {
        return "}";
    }

    /**
     * Functional Interface for rendering a node.
     */
    @FunctionalInterface
    protected interface NodeRenderer {

        String renderNode(final Node<PackageData> node);

    }

    /**
     * Functional Interface for rendering a usage.
     */
    @FunctionalInterface
    protected interface UsageRenderer {

        String renderUsage(final Node<PackageData> node);

    }

}

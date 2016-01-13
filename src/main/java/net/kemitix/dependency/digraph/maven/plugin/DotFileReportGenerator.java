package net.kemitix.dependency.digraph.maven.plugin;

import net.kemitix.node.Node;

import javax.inject.Inject;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Generates a dot file as dependency report generator.
 *
 * @author pcampbell
 */
class DotFileReportGenerator extends AbstractMojoService
        implements ReportGenerator {

    private Node<PackageData> base;

    private final NodePathGenerator nodeIdGenerator;

    /**
     * Injected constructor.
     *
     * @param nodeIdGenerator the node id generator
     */
    @Inject
    DotFileReportGenerator(final NodePathGenerator nodeIdGenerator) {
        this.nodeIdGenerator = nodeIdGenerator;
    }

    @Override
    public String generate(final Node<PackageData> baseNode) {
        base = baseNode;
        final StringBuilder report = new StringBuilder();
        report.append("digraph{compound=true;node[shape=box]\n");
        report.append(renderNode(base));
        report.append(renderUsages(base));
        report.append("}");
        return report.toString();
    }

    private String renderNode(final Node<PackageData> node) {
        final StringBuilder render = new StringBuilder();
        final String clusterId = nodeIdGenerator.getPath(node, base, "_");
        final String nodeId = node.getData().getName();
        final String headerFormat
                = "subgraph \"cluster{0}\"'{'"
                + "label=\"{1}\";\"{1}\"[style=dotted]\n";
        render.append(MessageFormat.format(headerFormat,
                clusterId, nodeId));

        node.getChildren().stream()
                .sorted(new NodePackageDataComparator())
                .forEach((Node<PackageData> child) -> {
                    if (child.getChildren().size() > 0) {
                        render.append(renderNode(child));
                    } else {
                        render.append(renderLeafPackage(child));
                    }
                });
        render.append("}\n");
        return render.toString();
    }

    private String renderUsages(final Node<PackageData> node) {
        final StringBuilder usages = new StringBuilder();
        node.getChildren().stream()
                .sorted(new NodePackageDataComparator())
                .forEach((Node<PackageData> childNode) -> {
                    childNode.getData().getUses().stream()
                            .filter((Node<PackageData> n) -> n.isChildOf(base))
                            .sorted(new NodePackageDataComparator())
                            .forEach((Node<PackageData> n) -> {
                                usages.append(renderUsage(childNode, n));
                            });
                    usages.append(renderUsages(childNode));
                });
        return usages.toString();
    }

    private String renderUsage(
            final Node<PackageData> tailNode,
            final Node<PackageData> headNode) {
        List<String> attributes = new ArrayList<>();
        // if tail node has children, then add ltail attribute
        if (tailNode.getChildren().size() > 0) {
            attributes.add(String.format("ltail=\"cluster%s\",",
                    nodeIdGenerator.getPath(tailNode, base, "_")));
        }
        // if head node has children, then add lhead attribute
        if (headNode.getChildren().size() > 0) {
            attributes.add(String.format("lhead=\"cluster%s\",",
                    nodeIdGenerator.getPath(headNode, base, "_")));
        }
        final StringBuilder attributeTag = new StringBuilder();
        if (attributes.size() > 0) {
            attributeTag.append("[");
            attributes.forEach(attributeTag::append);
            attributeTag.append("]");
        }

        return String.format("\"%s\"->\"%s\"%s%n",
                nodeIdGenerator.getPath(tailNode, base, "."),
                nodeIdGenerator.getPath(headNode, base, "."),
                attributeTag);
    }

    private String renderLeafPackage(final Node<PackageData> node) {
        return String.format("\"%s\"[label=\"%s\"];",
                nodeIdGenerator.getPath(node, base, "."),
                node.getData().getName());
    }

    /**
     * Comparator for sorting {@link PackageData} {@link Node}s.
     */
    @SuppressWarnings("serial")
    private static class NodePackageDataComparator
            implements Comparator<Node<PackageData>>, Serializable {

        NodePackageDataComparator() {
        }

        @Override
        public int compare(
                final Node<PackageData> o1,
                final Node<PackageData> o2) {
            return o1.getData().getName()
                    .compareTo(o2.getData().getName());
        }
    }

}

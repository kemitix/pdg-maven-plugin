package net.kemitix.dependency.digraph.maven.plugin;

import net.kemitix.node.Node;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates a dot file dependency report as nested clusters.
 *
 * @author pcampbell
 */
class DotFileFormatNested implements DotFileFormat {

    private final Node<PackageData> base;

    private final NodePathGenerator nodePathGenerator;

    /**
     * Constructor.
     *
     * @param base              the base package
     * @param nodePathGenerator the node path generator
     */
    DotFileFormatNested(
            final Node<PackageData> base,
            final NodePathGenerator nodePathGenerator) {
        this.base = base;
        this.nodePathGenerator = nodePathGenerator;
    }

    @Override
    public String renderReport() {
        final StringBuilder report = new StringBuilder();
        report.append("digraph{compound=true;node[shape=box]\n");
        report.append(renderNode(base));
        report.append(renderUsages(base));
        report.append("}");
        return report.toString();
    }

    private String renderNode(final Node<PackageData> node) {
        final StringBuilder render = new StringBuilder();
        final String clusterId = nodePathGenerator.getPath(node, base, "_");
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
                    nodePathGenerator.getPath(tailNode, base, "_")));
        }
        // if head node has children, then add lhead attribute
        if (headNode.getChildren().size() > 0) {
            attributes.add(String.format("lhead=\"cluster%s\",",
                    nodePathGenerator.getPath(headNode, base, "_")));
        }
        final StringBuilder attributeTag = new StringBuilder();
        if (attributes.size() > 0) {
            attributeTag.append("[");
            attributes.forEach(attributeTag::append);
            attributeTag.append("]");
        }

        return String.format("\"%s\"->\"%s\"%s%n",
                nodePathGenerator.getPath(tailNode, base, "."),
                nodePathGenerator.getPath(headNode, base, "."),
                attributeTag);
    }

    private String renderLeafPackage(final Node<PackageData> node) {
        return String.format("\"%s\"[label=\"%s\"];",
                nodePathGenerator.getPath(node, base, "."),
                node.getData().getName());
    }

}

package net.kemitix.dependency.digraph.maven.plugin;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import net.kemitix.node.Node;

/**
 * Generates a dot file dependency report as nested clusters.
 *
 * @author pcampbell
 */
class DotFileFormatNested extends AbstractDotFileFormat {

    private final NodePackageDataComparator nodePackageDataComparator
            = new NodePackageDataComparator();

    DotFileFormatNested(
            final Node<PackageData> base,
            final NodePathGenerator nodePathGenerator) {
        super(base, nodePathGenerator);
    }

    private String renderChild(final Node<PackageData> child) {
        if (child.getChildren().size() > 0) {
            return renderNode(child);
        } else {
            return renderSimpleNode(child);
        }
    }

    @Override
    String renderNode(final Node<PackageData> node) {
        final StringBuilder render = new StringBuilder();
        render.append(openSubgraph(node));
        node.getChildren()
            .stream()
            .sorted(nodePackageDataComparator)
            .map(this::renderChild)
            .forEach(render::append);
        render.append("}\n"); // close subgraph
        return render.toString();
    }

    private String openSubgraph(final Node<PackageData> node) {
        return MessageFormat.format("subgraph {0}'{'" + "label={1};"
                        + "{2}[label={1},style=dotted]\n", // phantom node
                getClusterId(node), // {0}
                getNodeName(node), // {1}
                getNodeId(node)); // {2}
    }

    @Override
    String renderUsages(final Node<PackageData> node) {
        final StringBuilder usages = new StringBuilder();
        node.getChildren()
            .stream()
            .sorted(nodePackageDataComparator)
            .forEach((Node<PackageData> childNode) -> {
                childNode.getData()
                         .getUses()
                         .stream()
                         .filter(this::isChildOfBase)
                         .sorted(nodePackageDataComparator)
                         .map(usedNode -> renderUsage(childNode, usedNode))
                         .forEach(usages::append);
                usages.append(renderUsages(childNode));
            });
        return usages.toString();
    }

    private boolean isChildOfBase(final Node<PackageData> node) {
        return node.isChildOf(getBase());
    }

    private String renderUsage(
            final Node<PackageData> tailNode,
            final Node<PackageData> headNode) {
        List<String> attributes = new ArrayList<>();
        // if tail node has children, then add ltail attribute
        if (tailNode.getChildren().size() > 0 && !headNode.isChildOf(
                tailNode)) {
            attributes.add(String.format("ltail=%s,", getClusterId(tailNode)));
        }
        // if head node has children, then add lhead attribute
        if (headNode.getChildren().size() > 0 && !tailNode.isChildOf(
                headNode)) {
            attributes.add(String.format("lhead=%s,", getClusterId(headNode)));
        }
        final StringBuilder attributeTag = new StringBuilder();
        if (attributes.size() > 0) {
            attributeTag.append("[");
            attributes.forEach(attributeTag::append);
            attributeTag.append("]");
        }

        return String.format("%s->%s%s%n", getNodeId(tailNode),
                getNodeId(headNode), attributeTag);
    }

}

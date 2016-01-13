package net.kemitix.dependency.digraph.maven.plugin;

import lombok.Getter;
import net.kemitix.node.Node;
import net.kemitix.node.NodeItem;
import org.apache.maven.plugin.logging.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of {@link DependencyData} using a node tree.
 *
 * @author Paul Campbell
 */
class NodeTreeDependencyData implements DependencyData {

    private final Node<PackageData> root
            = new NodeItem<>(new PackageData("[root]"));

    @Getter
    private Node<PackageData> baseNode;

    /**
     * Sets the base package.
     *
     * @param basePackage the base package within which to report
     */
    @Override
    public void setBasePackage(final String basePackage) {
        final List<PackageData> baseLine = createPackageLineList(basePackage);
        root.createDescendantLine(baseLine);
        root.walkTree(baseLine).ifPresent((Node<PackageData> base)
                -> baseNode = base);
    }

    @Override
    public void addDependency(final String user, final String imported) {
        final List<PackageData> userLine = createPackageLineList(user);
        root.createDescendantLine(userLine);
        final List<PackageData> importedLine
                = createPackageLineList(imported);
        root.createDescendantLine(importedLine);
        root.walkTree(importedLine).ifPresent((Node<PackageData> i) -> {
            root.walkTree(userLine).ifPresent((Node<PackageData> u) -> {
                u.getData().getUses().add(i);
            });
        });
    }

    private List<PackageData> createPackageLineList(final String userPackage) {
        List<PackageData> line = new ArrayList<>();
        Arrays.asList(userPackage.split("\\."))
                .forEach((String n) -> line.add(new PackageData(n)));
        return line;
    }

    @Override
    public void dumpDependencies(final Log log) {
        log.info("Packages:");
        logUsage(root);
        root.getChildren().stream().map(this::logUsage).forEach(log::info);
    }

    private String logUsage(final Node<PackageData> node) {
        final PackageData packageData = node.getData();
        final String userName = packageData.getName();
        final StringBuilder usages = new StringBuilder();
        packageData.getUses()
                .forEach((Node<PackageData> t) -> {
                    usages.append("* ").append(userName).append(" => ")
                            .append(t.getData().getName());
                });
        return usages.toString();
    }

}

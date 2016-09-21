package net.kemitix.dependency.digraph.maven.plugin;

import lombok.Getter;
import org.apache.maven.plugin.logging.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.concurrent.Immutable;

import net.kemitix.node.Node;
import net.kemitix.node.Nodes;

/**
 * Implementation of {@link DependencyData} using a node tree.
 *
 * @author Paul Campbell
 */
@Immutable
final class NodeTreeDependencyData implements DependencyData {

    private final Node<PackageData> root = Nodes.unnamedRoot(
            PackageData.newInstance("[root]"));

    @Getter
    private final Node<PackageData> baseNode;

    private NodeTreeDependencyData(final String basePackage) {
        final List<PackageData> baseLine = createPackageLineList(basePackage);
        root.createDescendantLine(baseLine);
        baseNode = root.findInPath(baseLine).orElse(null);
    }

    static DependencyData newInstance(final String basePackage) {
        return new NodeTreeDependencyData(basePackage);
    }

    @Override
    public void addDependency(final String user, final String imported) {
        final List<PackageData> userLine = createPackageLineList(user);
        root.createDescendantLine(userLine);
        final List<PackageData> importedLine = createPackageLineList(imported);
        root.createDescendantLine(importedLine);
        root.findInPath(importedLine)
            .ifPresent(i -> root.findInPath(userLine)
                                .ifPresent(u -> u.getData()
                                                 .ifPresent(d -> d.uses(i))));
    }

    @Override
    public void debugLog(final Log log) {
        debugLogNode(log, baseNode, 0);
    }

    private List<PackageData> createPackageLineList(final String userPackage) {
        List<PackageData> line = new ArrayList<>();
        Arrays.asList(userPackage.split("\\."))
              .forEach((String n) -> line.add(PackageData.newInstance(n)));
        return line;
    }

    private void debugLogNode(
            final Log log, final Node<PackageData> node, final int depth) {
        String padding = IntStream.range(0, depth * 2)
                                  .mapToObj(x -> " ")
                                  .collect(Collectors.joining());
        node.getData()
            .map(PackageData::getName)
            .map(name -> padding + name)
            .ifPresent(log::info);
        node.getChildren().forEach(t -> debugLogNode(log, t, depth + 1));
    }

}

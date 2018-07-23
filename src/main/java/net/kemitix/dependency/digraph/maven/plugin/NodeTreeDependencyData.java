/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Paul Campbell
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.kemitix.dependency.digraph.maven.plugin;

import lombok.Getter;
import net.kemitix.node.Node;
import net.kemitix.node.Nodes;
import org.apache.maven.plugin.logging.Log;

import javax.annotation.concurrent.Immutable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Implementation of {@link DependencyData} using a node tree.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@Immutable
final class NodeTreeDependencyData implements DependencyData {

    private final Node<PackageData> root = Nodes.namedRoot(PackageData.newInstance("[root]"), "root");

    @Getter
    private final Node<PackageData> baseNode;

    private NodeTreeDependencyData(final String basePackage) {
        final List<PackageData> baseLine = createPackageLineList(basePackage);
        root.createDescendantLine(baseLine);
        baseNode = root.findInPath(baseLine)
                       .orElse(null);
    }

    /**
     * Creates a new instance of DependencyData.
     *
     * @param basePackage The root node for the dependency data
     *
     * @return the DependencyData
     */
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
                                .ifPresent(u -> u.findData()
                                                 .ifPresent(d -> d.uses(i))));
    }

    @Override
    public void debugLog(final Log log) {
        debugLogNode(log, baseNode, 0);
    }

    @Override
    public void updateNames() {
        baseNode.stream()
                .forEach(node -> node.setName(node.getData()
                                                  .getName()));
    }

    private List<PackageData> createPackageLineList(final String userPackage) {
        List<PackageData> line = new ArrayList<>();
        Arrays.asList(userPackage.split("\\."))
              .forEach((String n) -> line.add(PackageData.newInstance(n)));
        return line;
    }

    private void debugLogNode(
            final Log log, final Node<PackageData> node, final int depth
                             ) {
        String padding = IntStream.range(0, depth * 2)
                                  .mapToObj(x -> " ")
                                  .collect(Collectors.joining());
        node.findData()
            .map(PackageData::getName)
            .map(name -> padding + name)
            .ifPresent(log::info);
        node.getChildren()
            .forEach(t -> debugLogNode(log, t, depth + 1));
    }

}

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

package net.kemitix.pdg.maven;

import lombok.val;
import net.kemitix.node.Node;
import net.kemitix.node.Nodes;
import net.kemitix.pdg.maven.digraph.PackageData;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Default implementation on {@link TreeFilter}.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@Named
class DefaultTreeFilter implements TreeFilter {

    private final GraphFilter graphFilter;
    private final NodePathGenerator nodePathGenerator;

    /**
     * Constructor.
     *
     * @param graphFilter       The graph filter
     * @param nodePathGenerator The node path generator
     */
    @Inject
    public DefaultTreeFilter(
            final GraphFilter graphFilter,
            final NodePathGenerator nodePathGenerator
    ) {
        this.graphFilter = graphFilter;
        this.nodePathGenerator = nodePathGenerator;
    }

    @Override
    public Node<PackageData> filterTree(final Node<PackageData> root) {
        val selected = root.stream()
                           .filter(graphFilter::filterNodes)
                           .collect(Collectors.toSet());
        val validNodes = Stream.concat(selected.stream(), Stream.concat(used(selected), users(root, selected)))
                               .distinct()
                               .flatMap(node -> Stream.concat(Stream.of(node), node.parentStream()))
                               .distinct()
                               .collect(Collectors.toList());
        val duplicateTree = duplicateNode(root, validNodes);
        fixUpUses(duplicateTree, root);
        return duplicateTree;
    }

    private void fixUpUses(
            final Node<PackageData> target,
            final Node<PackageData> root
    ) {
        val targetMap = target.stream()
                              .collect(nodeMapCollector(target));
        target.stream()
              .map(Node::getData)
              .forEach(data -> data.setUses(data.getUses()
                                                .stream()
                                                .map(use -> packageName(root, use))
                                                .filter(targetMap::containsKey)
                                                .map(targetMap::get)
                                                .collect(Collectors.toSet())));
        target.stream()
              .forEach(node -> node.setName(node.getData()
                                                .getName()));
    }

    private String packageName(
            final Node<PackageData> root,
            final Node<PackageData> use
    ) {
        return nodePathGenerator.getPath(use, root, ".");
    }

    private Collector<Node<PackageData>, ?, Map<String, Node<PackageData>>> nodeMapCollector(
            final Node<PackageData> legacy
    ) {
        return Collectors.toMap(node -> packageName(legacy, node), Function.identity());
    }

    // get list of nodes to be included because they use an included node
    private Stream<Node<PackageData>> users(
            final Node<PackageData> root,
            final Set<Node<PackageData>> included
    ) {
        return root.stream()
                   .flatMap(node -> node.getData()
                                        .getUses()
                                        .stream()
                                        .filter(included::contains)
                                        .map(usedNode -> node))
                   .filter(node -> !graphFilter.isExcluded(node));
    }

    // get list of nodes to be included because they are used by an included node
    private Stream<Node<PackageData>> used(final Set<Node<PackageData>> included) {
        return included.stream()
                       .flatMap(node -> node.getData()
                                            .getUses()
                                            .stream()
                                            .filter(use -> !graphFilter.isExcluded(use)));
    }

    private Node<PackageData> duplicateNode(
            final Node<PackageData> source,
            final List<Node<PackageData>> eligible
    ) {
        final Node<PackageData> duplicate = Nodes.namedRoot(null, source.getName());
        duplicate.setData(source.getData());
        source.getChildren()
              .stream()
              .filter(eligible::contains)
              .map(child -> duplicateNode(child, eligible))
              .forEach(duplicate::addChild);
        return duplicate;
    }
}

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

import net.kemitix.dependency.digraph.maven.plugin.digraph.EdgeElement;
import net.kemitix.dependency.digraph.maven.plugin.digraph.GraphElement;
import net.kemitix.dependency.digraph.maven.plugin.digraph.NodeElement;
import net.kemitix.dependency.digraph.maven.plugin.digraph.Subgraph;
import net.kemitix.node.Node;

import javax.annotation.concurrent.Immutable;
import java.util.stream.Collectors;

/**
 * Generates a dot file dependency report as a simple digraph.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@Immutable
class DotFileFormatSimple extends AbstractDotFileFormat {

    public static final String NEWLINE = System.lineSeparator();

    /**
     * Constructor.
     *
     * @param base              The root node
     * @param nodePathGenerator The Node Path Generator
     * @param graphFilter       The Graph Filter
     */
    DotFileFormatSimple(
            final Node<PackageData> base, final NodePathGenerator nodePathGenerator, final GraphFilter graphFilter
                       ) {
        super(base, nodePathGenerator, graphFilter);
    }

    @Override
    public String render(final Subgraph subgraph) {
        final String id = subgraph.getId();
        String node = "";
        if (!id.startsWith("_")) {
            node = quoted(id) + NEWLINE;
        }
        return node + subgraph.getElements()
                              .stream()
                              .map(GraphElement::render)
                              .collect(Collectors.joining(NEWLINE));
    }

    @Override
    public String render(final NodeElement nodeElement) {
        return quoted(nodeElement.getId());
    }

    @Override
    public String render(final EdgeElement edgeElement) {
        return quoted(edgeElement.getTail()
                                 .getId()) + " -> " + quoted(edgeElement.getHead()
                                                                        .getId());
    }

}

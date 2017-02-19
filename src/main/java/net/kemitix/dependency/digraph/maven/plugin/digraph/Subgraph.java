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

package net.kemitix.dependency.digraph.maven.plugin.digraph;

import lombok.Getter;
import net.kemitix.dependency.digraph.maven.plugin.DotFileFormat;
import net.kemitix.dependency.digraph.maven.plugin.PackageData;
import net.kemitix.node.Node;

import javax.annotation.concurrent.Immutable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represets a subgraph/cluster.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@Getter
@Immutable
public class Subgraph extends AbstractGraphElement implements ElementContainer, HasId, HasLabel, EdgeEndpoint {

    private final List<GraphElement> elements = new ArrayList<>();

    private final String id;

    private final String label;

    private final Node<PackageData> packageDataNode;

    /**
     * Constructor.
     *
     * @param packageDataNode the package data node for this subgraph
     * @param id              the id of the subgraph
     * @param label           the label of the subgraph
     * @param dotFileFormat   the output format
     */
    public Subgraph(
            final Node<PackageData> packageDataNode, final String id, final String label,
            final DotFileFormat dotFileFormat
                   ) {
        super(dotFileFormat);
        this.packageDataNode = packageDataNode;
        this.id = id;
        this.label = label;
    }

    @Override
    public final boolean add(final GraphElement graphElement) {
        return elements.add(graphElement);
    }

    @Override
    public final String render() {
        return getDotFileFormat().render(this);
    }
}

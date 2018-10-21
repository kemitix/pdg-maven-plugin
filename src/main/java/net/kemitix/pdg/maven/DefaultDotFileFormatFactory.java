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

import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Default implementation of the {@link DotFileFormatFactory}.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@Named
@Immutable
class DefaultDotFileFormatFactory implements DotFileFormatFactory {

    private final NodePathGenerator nodePathGenerator;
    private final GraphFilter graphFilter;
    private final TreeFilter treeFilter;

    /**
     * Constructors.
     *
     * @param nodePathGenerator The node path generator
     * @param graphFilter       The graph filter
     * @param treeFilter        The tree filter
     */
    @Inject
    public DefaultDotFileFormatFactory(
            final NodePathGenerator nodePathGenerator,
            final GraphFilter graphFilter,
            final TreeFilter treeFilter
    ) {
        this.nodePathGenerator = nodePathGenerator;
        this.graphFilter = graphFilter;
        this.treeFilter = treeFilter;
    }

    @Override
    public DotFileFormat create(final String format, final Node<PackageData> base) {
        val tree = treeFilter.filterTree(base);
        DotFileFormat reportFormat;
        switch (format) {
            case "simple":
                reportFormat = new DotFileFormatSimple(tree, nodePathGenerator, graphFilter);
                break;
            case "nested":
            default:
                reportFormat = new DotFileFormatNested(tree, nodePathGenerator, graphFilter);
                break;
        }
        return reportFormat;
    }
}

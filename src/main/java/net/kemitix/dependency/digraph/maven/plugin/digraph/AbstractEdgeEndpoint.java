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

import javax.annotation.concurrent.Immutable;

import net.kemitix.dependency.digraph.maven.plugin.DotFileFormat;
import net.kemitix.dependency.digraph.maven.plugin.NodeHelper;
import net.kemitix.dependency.digraph.maven.plugin.PackageData;
import net.kemitix.node.Node;

/**
 * .
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@Immutable
abstract class AbstractEdgeEndpoint extends AbstractGraphElement
        implements EdgeEndpoint {

    private final Node<PackageData> packageDataNode;

    /**
     * Constructor.
     *
     * @param dotFileFormat The generator for the Dot File
     * @param packageDataNode The Node containing the PackageData
     */
    AbstractEdgeEndpoint(
            final DotFileFormat dotFileFormat,
            final Node<PackageData> packageDataNode) {
        super(dotFileFormat);
        this.packageDataNode = packageDataNode;
    }

    /**
     * Creates a copy of the package data node and returns it.
     *
     * @return a copy of the package data node
     */
    public Node<PackageData> getPackageDataNode() {
        return NodeHelper.copyOf(packageDataNode);
    }
}

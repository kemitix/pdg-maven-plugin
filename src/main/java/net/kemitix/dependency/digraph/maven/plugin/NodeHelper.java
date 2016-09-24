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

package net.kemitix.dependency.digraph.maven.plugin;

import lombok.val;

import javax.annotation.concurrent.Immutable;

import net.kemitix.node.Node;
import net.kemitix.node.Nodes;

/**
 * Helpers for Node.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@Immutable
public final class NodeHelper {

    private NodeHelper() {
    }

    static PackageData getRequiredData(
            final Node<PackageData> node) {
        return node.getData()
                   .orElseThrow(() -> new IllegalStateException(
                           "Node has no package data"));
    }

    /**
     * Creates a copy of the package data node and returns it.
     *
     * @param source the node to copy
     *
     * @return a copy of the package data node
     */
    public static Node<PackageData> copyOf(final Node<PackageData> source) {
        val optionalData = source.getData();
        PackageData data = null;
        if (optionalData.isPresent()) {
            data = optionalData.get();
        }
        val optionalParent = source.getParent();
        Node<PackageData> parent = null;
        if (optionalParent.isPresent()) {
            parent = optionalParent.get();
        }
        if (parent != null) {
            return Nodes.namedChild(data, source.getName(), parent);
        }
        return Nodes.namedRoot(data, source.getName());
    }

}

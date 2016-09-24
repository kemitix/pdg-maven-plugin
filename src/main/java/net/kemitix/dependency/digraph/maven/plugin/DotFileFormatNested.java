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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.concurrent.Immutable;

import net.kemitix.dependency.digraph.maven.plugin.digraph.EdgeElement;
import net.kemitix.dependency.digraph.maven.plugin.digraph.NodeElement;
import net.kemitix.dependency.digraph.maven.plugin.digraph.Subgraph;
import net.kemitix.node.Node;

/**
 * Generates a dot file dependency report as nested clusters.
 *
 * @author pcampbell
 */
@Immutable
class DotFileFormatNested extends AbstractDotFileFormat {

    DotFileFormatNested(
            final Node<PackageData> base,
            final NodePathGenerator nodePathGenerator) {
        super(base, nodePathGenerator);
    }

    private void addAnyLHead(final Node<PackageData> headNode,
            final Node<PackageData> tailNode, final List<String> attributes) {
        // if head node has children, then add lhead attribute
        if (headNode.getChildren().size() > 0 && !tailNode.isDescendantOf(
                headNode)) {
            attributes.add(String.format("lhead=\"cluster%s\"",
                    getClusterId(headNode)));
        }
    }

    private void addAnyLTail(final Node<PackageData> tailNode,
            final Node<PackageData> headNode, final List<String> attributes) {
        // if tail node has children, then add ltail attribute
        if (tailNode.getChildren().size() > 0 && !headNode.isDescendantOf(
                tailNode)) {
            attributes.add(String.format("ltail=\"cluster%s\"",
                    getClusterId(tailNode)));
        }
    }

    private String buildAttributeTag(final List<String> attributes) {
        String attributeTag = "";
        if (attributes.size() > 0) {
            attributeTag = String.format("[%s]",
                    attributes.stream().collect(Collectors.joining(",")));
        }
        return attributeTag;
    }

    @Override
    public String render(final Subgraph subgraph) {
        final String label = quoted(subgraph.getLabel());
        final String id = quoted(subgraph.getId());
        return String.format("subgraph %s{%n" + "label=%s%n"
                + "%s[label=\"\",style=\"invis\",width=0]%n" + "%s%n}",
                quoted("cluster" + subgraph.getId()), label, id,
                renderElements(subgraph.getElements()));
    }

    @Override
    public String render(
            final EdgeElement edgeElement) {
        List<String> attributes = new ArrayList<>();
        final Node<PackageData> tailNode = edgeElement.getTail()
                .getPackageDataNode();
        final Node<PackageData> headNode = edgeElement.getHead()
                .getPackageDataNode();
        addAnyLTail(tailNode, headNode, attributes);
        addAnyLHead(headNode, tailNode, attributes);
        String attributeTag = buildAttributeTag(attributes);
        return String.format("%s->%s%s", quoted(getNodeId(tailNode)),
                quoted(getNodeId(headNode)), attributeTag);
    }

    @Override
    public final String render(final NodeElement nodeElement) {
        final String id = nodeElement.getId();
        final String label = nodeElement.getLabel();
        if (id.equals(label)) {
            return quoted(id);
        } else {
            return quoted(id) + "[label=" + quoted(label) + CLOSE_BRACE;
        }
    }

}

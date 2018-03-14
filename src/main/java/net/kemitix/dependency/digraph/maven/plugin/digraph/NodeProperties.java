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

package net.kemitix.dependency.digraph.maven.plugin.digraph;

import lombok.Getter;
import net.kemitix.dependency.digraph.maven.plugin.DotFileFormat;

import javax.annotation.concurrent.Immutable;
import java.util.HashSet;
import java.util.Set;

/**
 * Properties for the nodes that follow.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@Immutable
public class NodeProperties extends AbstractGraphElement {

    @Getter
    private final Set<PropertyElement> properties = new HashSet<>();

    /**
     * Constructor.
     *
     * @param dotFileFormat the output format
     */
    NodeProperties(final DotFileFormat dotFileFormat) {
        super(dotFileFormat);
    }

    /**
     * Adds a property to the node.
     *
     * @param propertyElement the property to add
     *
     * @return true if the property is added
     */
    public final boolean add(final PropertyElement propertyElement) {
        return properties.add(propertyElement);
    }

    @Override
    public final String render() {
        return getDotFileFormat().render(this);
    }
}

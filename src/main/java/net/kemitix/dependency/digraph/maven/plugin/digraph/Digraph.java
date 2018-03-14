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
import lombok.val;
import net.kemitix.dependency.digraph.maven.plugin.DotFileFormat;

import javax.annotation.concurrent.Immutable;
import java.util.ArrayList;
import java.util.List;

/**
 * A GraphViz Directed Graph root node.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@Immutable
public class Digraph extends AbstractGraphElement implements ElementContainer {

    /**
     * A list of ordered elements.
     *
     * <p>These are in the order that they are to be present in the generate
     * file.</p>
     */
    @Getter
    private final List<GraphElement> elements = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param dotFileFormat the output format
     */
    public Digraph(final DotFileFormat dotFileFormat) {
        super(dotFileFormat);
    }

    @Override
    public final boolean add(final GraphElement graphElement) {
        return elements.add(graphElement);
    }

    @Override
    public final String render() {
        return getDotFileFormat().render(this);
    }

    /**
     * Builder for Digraph.
     */
    public static class Builder {

        private final DotFileFormat dotFileFormat;

        /**
         * Constructor.
         *
         * @param dotFileFormat the output format
         */
        public Builder(final DotFileFormat dotFileFormat) {
            this.dotFileFormat = dotFileFormat;
        }

        /**
         * Builds a Digraph with preset properties and node properties.
         *
         * @return a Digraph
         */
        public final Digraph build() {
            val digraph = new Digraph(dotFileFormat);
            digraph.add(new PropertyElement("compound", "true", dotFileFormat));
            val nodeProperties = new NodeProperties(dotFileFormat);
            nodeProperties.add(new PropertyElement("shape", "box", dotFileFormat));
            digraph.add(nodeProperties);
            return digraph;
        }
    }
}

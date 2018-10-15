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

package net.kemitix.pdg.maven.digraph;

import lombok.Getter;
import net.kemitix.pdg.maven.DotFileFormat;

import javax.annotation.concurrent.Immutable;

/**
 * Represents an edge (line) on the graph.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@Getter
@Immutable
public class EdgeElement extends AbstractGraphElement {

    /**
     * Where the arrow is pointing from.
     */
    private final EdgeEndpoint tail;

    /**
     * Where the arrow is pointing to.
     */
    private final EdgeEndpoint head;

    /**
     * Constructor.
     *
     * @param tail          the tail (origin of the edge)
     * @param head          the head (destination of the edge)
     * @param dotFileFormat the output format
     */
    public EdgeElement(
            final EdgeEndpoint tail, final EdgeEndpoint head, final DotFileFormat dotFileFormat
                      ) {
        super(dotFileFormat);
        this.tail = tail;
        this.head = head;
    }

    @Override
    public final String render() {
        return getDotFileFormat().render(this);
    }
}

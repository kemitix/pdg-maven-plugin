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
 * A key, value property.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@Getter
@Immutable
public class PropertyElement extends AbstractGraphElement {

    private final String name;
    private final String value;

    /**
     * Constructor.
     *
     * @param name          the name of the property
     * @param value         the value of the property
     * @param dotFileFormat the output format
     */
    public PropertyElement(
            final String name,
            final String value,
            final DotFileFormat dotFileFormat
    ) {
        super(dotFileFormat);
        this.name = name;
        this.value = value;
    }

    @Override
    public final String render() {
        return getDotFileFormat().render(this);
    }
}

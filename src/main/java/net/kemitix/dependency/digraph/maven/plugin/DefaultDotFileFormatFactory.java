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

import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;

import net.kemitix.node.Node;

/**
 * Default implementation of the {@link DotFileFormatFactory}.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@Immutable
class DefaultDotFileFormatFactory implements DotFileFormatFactory {

    private final NodePathGenerator nodePathGenerator;

    /**
     * Constructor.
     *
     * @param nodePathGenerator The Node Path Generator
     */
    @Inject
    DefaultDotFileFormatFactory(final NodePathGenerator nodePathGenerator) {
        this.nodePathGenerator = nodePathGenerator;
    }

    @Override
    public DotFileFormat create(
            final String format, final Node<PackageData> base) {
        DotFileFormat reportFormat;
        switch (format) {
        case "simple":
            reportFormat = new DotFileFormatSimple(base, nodePathGenerator);
            break;
        case "nested":
        default:
            reportFormat = new DotFileFormatNested(base, nodePathGenerator);
            break;
        }
        return reportFormat;
    }
}

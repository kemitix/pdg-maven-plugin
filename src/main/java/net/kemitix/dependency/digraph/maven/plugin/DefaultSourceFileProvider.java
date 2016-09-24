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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;

/**
 * Provider walks the directory and builds a list of discovered Java files.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@Immutable
class DefaultSourceFileProvider implements SourceFileProvider {

    private final SourceFileVisitor fileVisitor;

    private final DigraphMojo mojo;

    /**
     * Constructor.
     *
     * @param fileVisitor The File Visitor
     * @param mojo        The Maven Mojo
     */
    @Inject
    DefaultSourceFileProvider(
            final SourceFileVisitor fileVisitor, final DigraphMojo mojo) {
        this.fileVisitor = fileVisitor;
        this.mojo = mojo;
    }

    @Override
    public List<File> process(final List<String> directories) {
        directories.forEach((final String dir) -> {
            try {
                Path start = new File(dir).getAbsoluteFile().toPath();
                Files.walkFileTree(start, fileVisitor);
            } catch (IOException ex) {
                mojo.getLog().error(ex);
            }
        });
        return fileVisitor.getJavaFiles();
    }

}

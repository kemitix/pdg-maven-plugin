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

package net.kemitix.pdg.maven.scan;

import net.kemitix.pdg.maven.DigraphConfiguration;

import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Provider walks the directory and builds a list of discovered Java files.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@Named("sources")
@Immutable
class DefaultSourceFileProvider implements SourceFileProvider {

    private final SourceFileVisitor fileVisitor;
    private final DigraphConfiguration digraphConfiguration;

    /**
     * Constructor.
     *
     * @param fileVisitor   The file visitor
     * @param configuration The configuration
     */
    @Inject
    public DefaultSourceFileProvider(
            final SourceFileVisitor fileVisitor,
            final DigraphConfiguration configuration
    ) {
        this.fileVisitor = fileVisitor;
        this.digraphConfiguration = configuration;
    }

    @Override
    public List<File> process(final List<String> directories) {
        directories.forEach((final String dir) -> {
            try {
                Path start = new File(dir).getAbsoluteFile()
                                          .toPath();
                Files.walkFileTree(start, fileVisitor);
            } catch (IOException ex) {
                digraphConfiguration.getLog()
                    .error(ex);
            }
        });
        return fileVisitor.getJavaFiles();
    }

}

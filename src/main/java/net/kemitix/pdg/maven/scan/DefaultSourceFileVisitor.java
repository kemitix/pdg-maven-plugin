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

import lombok.Getter;

import javax.annotation.concurrent.Immutable;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the source file visitor.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@Immutable
class DefaultSourceFileVisitor extends SimpleFileVisitor<Path> implements SourceFileVisitor {

    /**
     * The list of Java files discovered.
     */
    @Getter
    private final List<File> javaFiles = new ArrayList<>();

    @Override
    public FileVisitResult visitFile(
            final Path file, final BasicFileAttributes attrs
                                    ) throws IOException {
        if (file.toString()
                .endsWith(".java")) {
            javaFiles.add(file.toFile());
        }
        return FileVisitResult.CONTINUE;
    }

}

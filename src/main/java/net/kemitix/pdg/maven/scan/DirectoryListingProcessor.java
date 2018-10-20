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

import lombok.RequiredArgsConstructor;
import lombok.val;
import net.kemitix.pdg.maven.DigraphConfiguration;

import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Processor to list directories to console.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@Immutable
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
class DirectoryListingProcessor implements SourceFileProvider {

    private final DigraphConfiguration configuration;

    @Override
    public List<File> process(final List<String> directories) {
        val log = configuration.getLog();
        directories.stream()
                   .map(d -> "* " + d)
                   .forEach(log::info);
        return new ArrayList<>();
    }

}

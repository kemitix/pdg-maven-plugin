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

import lombok.val;
import net.kemitix.pdg.maven.DependencyData;
import net.kemitix.pdg.maven.DigraphConfiguration;
import net.kemitix.pdg.maven.PackageScanner;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Implementation of {@link PackageScanner}.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@Named
class PackageScannerImpl implements PackageScanner {

    private final SourceDirectoryProvider directoryProvider;
    private final SourceFileProvider fileProvider;
    private final FileLoader fileLoader;
    private final SourceFileAnalyser fileAnalyser;

    /**
     * Constructor.
     *
     * @param directoryProvider The directory provider
     * @param fileProvider      The file provider
     * @param fileLoader        The file loader
     * @param fileAnalyser      The file analyser
     */
    @Inject
    public PackageScannerImpl(
            final SourceDirectoryProvider directoryProvider,
            final SourceFileProvider fileProvider,
            final FileLoader fileLoader,
            final SourceFileAnalyser fileAnalyser
    ) {
        this.directoryProvider = directoryProvider;
        this.fileProvider = fileProvider;
        this.fileLoader = fileLoader;
        this.fileAnalyser = fileAnalyser;
    }

    /**
     * Scans the base package.
     *
     * @param configuration the mojo configuration
     *
     * @return the dependency data
     */
    @Override
    public DependencyData scan(final DigraphConfiguration configuration) {
        val dependencyData = DigraphFactory.newDependencyData(configuration.getBasePackage());
        final List<String> directories =
                directoryProvider.getDirectories(configuration.getProjects(), configuration.isIncludeTests());
        fileProvider.process(directories)
                .stream()
                .map(fileLoader::asInputStream)
                .forEach(in -> fileAnalyser.analyse(dependencyData, in));
        dependencyData.updateNames();
        return dependencyData;
    }
}

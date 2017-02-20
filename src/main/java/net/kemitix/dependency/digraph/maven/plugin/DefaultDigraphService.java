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

import lombok.val;

import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;
import java.io.IOException;

/**
 * Default implementation of the Digraph Service.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@Immutable
class DefaultDigraphService implements DigraphService {

    private static final String REPORT_FILE = "target/digraph.dot";

    private final SourceDirectoryProvider directoryProvider;

    private final SourceFileProvider fileProvider;

    private final FileLoader fileLoader;

    private final SourceFileAnalyser fileAnalyser;

    private final ReportGenerator reportGenerator;

    private final ReportWriter reportWriter;

    private final DotFileFormatFactory dotFileFormatFactory;

    /**
     * Constructor.
     *
     * @param directoryProvider    The Directory Provider
     * @param fileProvider         The File Provider
     * @param fileLoader           The File Loader
     * @param fileAnalyser         The File Analyser
     * @param reportGenerator      The Report Generator
     * @param reportWriter         The Report Writer
     * @param dotFileFormatFactory The Dot File Format Factory
     */
    @Inject
    DefaultDigraphService(
            final SourceDirectoryProvider directoryProvider, final SourceFileProvider fileProvider,
            final FileLoader fileLoader, final SourceFileAnalyser fileAnalyser, final ReportGenerator reportGenerator,
            final ReportWriter reportWriter, final DotFileFormatFactory dotFileFormatFactory
                         ) {
        this.directoryProvider = directoryProvider;
        this.fileProvider = fileProvider;
        this.fileLoader = fileLoader;
        this.fileAnalyser = fileAnalyser;
        this.reportGenerator = reportGenerator;
        this.reportWriter = reportWriter;
        this.dotFileFormatFactory = dotFileFormatFactory;
    }

    @Override
    public void execute(final DigraphMojo mojo) {
        val dependencyData = DigraphFactory.newDependencyData(mojo.getBasePackage());
        fileProvider.process(directoryProvider.getDirectories(mojo.getProjects(), mojo.isIncludeTests()))
                    .stream()
                    .map(fileLoader::asInputStream)
                    .forEach(in -> fileAnalyser.analyse(dependencyData, in));
        dependencyData.updateNames();
        if (mojo.isDebug()) {
            dependencyData.debugLog(mojo.getLog());
        }
        try {
            reportWriter.write(reportGenerator.generate(
                    dotFileFormatFactory.create(mojo.getFormat(), dependencyData.getBaseNode())), REPORT_FILE);
        } catch (IOException ex) {
            mojo.getLog()
                .error(ex.getMessage());
        }
    }
}

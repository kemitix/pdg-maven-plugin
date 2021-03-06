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

package net.kemitix.pdg.maven;

import lombok.val;

import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.IOException;

/**
 * Default implementation of the Digraph Service.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@Named
@Immutable
class DefaultDigraphService implements DigraphService {

    private static final String REPORT_FILE = "digraph.dot";

    private final PackageScanner packageScanner;
    private final ReportGenerator reportGenerator;
    private final ReportWriter reportWriter;
    private final DotFileFormatFactory dotFileFormatFactory;

    /**
     * Constructor.
     *
     * @param packageScanner       The package scanner
     * @param reportGenerator      The report generator
     * @param reportWriter         The report writer
     * @param dotFileFormatFactory The DOT file format factory
     */
    @Inject
    public DefaultDigraphService(
            final PackageScanner packageScanner,
            final ReportGenerator reportGenerator,
            final ReportWriter reportWriter,
            final DotFileFormatFactory dotFileFormatFactory
    ) {
        this.packageScanner = packageScanner;
        this.reportGenerator = reportGenerator;
        this.reportWriter = reportWriter;
        this.dotFileFormatFactory = dotFileFormatFactory;
    }

    @Override
    @SuppressWarnings("npathcomplexity")
    public void execute(final DigraphConfiguration configuration) {
        final DependencyData dependencyData = packageScanner.scan(configuration);

        if (configuration.isDebug()) {
            dependencyData.debugLog(configuration.getLog());
        }
        try {
            val outputDirectory = new File(configuration.getProject()
                                               .getBuild()
                                               .getDirectory());
            outputDirectory.mkdirs();
            reportWriter.write(
                    reportGenerator.generate(
                            dotFileFormatFactory.create(configuration.getFormat(), dependencyData.getBaseNode())),
                    new File(outputDirectory, REPORT_FILE).getAbsolutePath()
                              );
        } catch (IOException ex) {
            configuration.getLog()
                    .error(ex.getMessage());
        }
    }
}

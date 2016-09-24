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
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.util.List;

import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;

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

    @Inject
    DefaultDigraphService(
            final SourceDirectoryProvider directoryProvider,
            final SourceFileProvider fileProvider, final FileLoader fileLoader,
            final SourceFileAnalyser fileAnalyser,
            final ReportGenerator reportGenerator,
            final ReportWriter reportWriter,
            final DotFileFormatFactory dotFileFormatFactory) {
        this.directoryProvider = directoryProvider;
        this.fileProvider = fileProvider;
        this.fileLoader = fileLoader;
        this.fileAnalyser = fileAnalyser;
        this.reportGenerator = reportGenerator;
        this.reportWriter = reportWriter;
        this.dotFileFormatFactory = dotFileFormatFactory;
    }

    @Override
    public void execute(
            final AbstractMojo mojo, final List<MavenProject> projects,
            final boolean includeTests, final String basePackage,
            final String format, final boolean debug) {
        val dependencyData = DigraphFactory.newDependencyData(basePackage);
        fileProvider.process(
                directoryProvider.getDirectories(projects, includeTests))
                    .stream()
                    .map(fileLoader::asInputStream)
                    .forEach(in -> fileAnalyser.analyse(dependencyData, in));
        if (debug) {
            dependencyData.debugLog(mojo.getLog());
        }
        try {
            reportWriter.write(reportGenerator.generate(
                    dotFileFormatFactory.create(format,
                            dependencyData.getBaseNode())), REPORT_FILE);
        } catch (IOException ex) {
            mojo.getLog().error(ex.getMessage());
        }
    }
}

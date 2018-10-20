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

import com.google.inject.AbstractModule;
import lombok.RequiredArgsConstructor;
import net.kemitix.pdg.maven.scan.ScanModule;

import javax.annotation.concurrent.Immutable;

/**
 * Google Guice Configuration.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@Immutable
@RequiredArgsConstructor
class DigraphModule extends AbstractModule {

    private final DigraphConfiguration digraphConfiguration;
    private final GraphFilter graphFilter;

    @Override
    @SuppressWarnings("javancss")
    protected void configure() {
        bind(DigraphConfiguration.class).toInstance(digraphConfiguration);
        bind(GraphFilter.class).toInstance(graphFilter);
        bind(DigraphService.class).to(DefaultDigraphService.class);
        this.install(new ScanModule());
        bind(DotFileFormatFactory.class).to(DefaultDotFileFormatFactory.class);
        bind(ReportGenerator.class).to(DotFileReportGenerator.class);
        bind(ReportWriter.class).to(DefaultReportWriter.class);
        bind(NodePathGenerator.class).to(DefaultNodePathGenerator.class);
        bind(TreeFilter.class).to(DefaultTreeFilter.class);
    }

}

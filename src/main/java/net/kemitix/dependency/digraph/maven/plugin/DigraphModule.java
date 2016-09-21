package net.kemitix.dependency.digraph.maven.plugin;

import com.google.inject.AbstractModule;

import javax.annotation.concurrent.Immutable;

/**
 * Google Guice Configuration.
 *
 * @author pcampbell
 */
@Immutable
class DigraphModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DigraphService.class).to(DefaultDigraphService.class);
        bind(DotFileFormatFactory.class).to(DefaultDotFileFormatFactory.class);
        bind(SourceDirectoryProvider.class).to(
                DefaultSourceDirectoryProvider.class);
        bind(SourceFileProvider.class).to(DefaultSourceFileProvider.class);
        bind(SourceFileVisitor.class).to(DefaultSourceFileVisitor.class);
        bind(FileLoader.class).to(DefaultFileLoader.class);
        bind(SourceFileAnalyser.class).to(DefaultSourceFileAnalyser.class);
        bind(ReportGenerator.class).to(DotFileReportGenerator.class);
        bind(ReportWriter.class).to(DefaultReportWriter.class);
        bind(NodePathGenerator.class).to(DefaultNodePathGenerator.class);
    }

}

package net.kemitix.dependency.digraph.maven.plugin;

import com.google.inject.AbstractModule;

/**
 * Google Guice Configuration.
 *
 * @author pcampbell
 */
class DigraphModule extends AbstractModule {

    private final DependencyData dependencyData = new NodeTreeDependencyData();

    @Override
    protected void configure() {
        bind(SourceDirectoryProvider.class).to(
                DefaultSourceDirectoryProvider.class);
        bind(SourceFileProvider.class).to(DefaultSourceFileProvider.class);
        bind(SourceFileVisitor.class).to(DefaultSourceFileVisitor.class);
        bind(FileLoader.class).to(DefaultFileLoader.class);
        bind(SourceFileAnalyser.class).to(DefaultSourceFileAnalyser.class);
        bind(DependencyData.class).toInstance(dependencyData);
        bind(ReportGenerator.class).to(DotFileReportGenerator.class);
        bind(ReportWriter.class).to(DefaultReportWriter.class);
        bind(PackageTreeBuilder.class).to(DefaultPackageTreeBuilder.class);
        bind(NodePathGenerator.class).to(DefaultNodePathGenerator.class);
    }

}

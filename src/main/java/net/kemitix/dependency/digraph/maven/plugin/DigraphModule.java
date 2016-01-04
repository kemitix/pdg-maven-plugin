package net.kemitix.dependency.digraph.maven.plugin;

import com.google.inject.AbstractModule;

/**
 * Google Guice Configuration.
 *
 * @author pcampbell
 */
public class DigraphModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SourceDirectoryProvider.class)
                .to(DefaultSourceDirectoryProvider.class);
        bind(SourceFileProvider.class)
                .to(DefaultSourceFileProvider.class);
        bind(SourceFileVisitor.class)
                .to(DefaultSourceFileVisitor.class);
    }

}

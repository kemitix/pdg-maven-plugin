package net.kemitix.dependency.digraph.maven.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;

/**
 * Implementation of {@link FileLoader}.
 *
 * @author pcampbell
 */
@Immutable
class DefaultFileLoader implements FileLoader {

    private final DigraphMojo mojo;

    @Inject
    DefaultFileLoader(final DigraphMojo mojo) {
        this.mojo = mojo;
    }

    @Override
    public InputStream asInputStream(final File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            mojo.getLog().error(ex);
        }
        return null;
    }

}

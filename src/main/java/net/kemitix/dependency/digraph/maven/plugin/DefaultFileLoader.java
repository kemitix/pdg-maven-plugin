package net.kemitix.dependency.digraph.maven.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Implementation of {@link FileLoader}.
 *
 * @author pcampbell
 */
public class DefaultFileLoader extends AbstractMojoService
        implements FileLoader {

    @Override
    public InputStream asInputStream(final File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            getLog().error(ex);
        }
        return null;
    }

}

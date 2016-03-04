package net.kemitix.dependency.digraph.maven.plugin;

import java.io.File;
import java.io.InputStream;

/**
 * Interface for loading a file.
 *
 * @author pcampbell
 */
interface FileLoader {

    /**
     * * Opens the file as in InputStream.
     *
     * @param file the file to open
     *
     * @return the file input stream or null if file not found
     */
    InputStream asInputStream(File file);

}

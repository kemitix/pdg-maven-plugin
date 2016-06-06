package net.kemitix.dependency.digraph.maven.plugin;

import java.io.InputStream;

/**
 * Interface for a source code file analyser.
 *
 * @author pcampbell
 */
interface SourceFileAnalyser {

    /**
     * Analyse the file.
     *
     * @param input the stream of the file to analyse
     */
    void analyse(InputStream input);

}

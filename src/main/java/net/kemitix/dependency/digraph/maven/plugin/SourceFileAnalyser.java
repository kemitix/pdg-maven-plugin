package net.kemitix.dependency.digraph.maven.plugin;

import java.io.File;

/**
 * Interface for a source code file analyser.
 *
 * @author pcampbell
 */
interface SourceFileAnalyser extends MojoService {

    /**
     * Analyse the file.
     *
     * @param file the file to analyse
     */
    void analyse(File file);

}

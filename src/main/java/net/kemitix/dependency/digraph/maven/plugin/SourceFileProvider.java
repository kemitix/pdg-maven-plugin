package net.kemitix.dependency.digraph.maven.plugin;

import java.io.File;
import java.util.List;

/**
 * Interface for processing a list of directories.
 *
 * @author pcampbell
 */
interface SourceFileProvider extends MojoService {

    /**
     * Process the list of directories.
     *
     * @param directories the directories to process
     */
    void process(List<String> directories);

    /**
     * Returns the list of Java file.
     *
     * @return the list of Java files
     */
    List<File> getJavaFiles();

}

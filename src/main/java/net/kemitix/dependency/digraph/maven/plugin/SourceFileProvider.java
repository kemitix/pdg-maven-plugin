package net.kemitix.dependency.digraph.maven.plugin;

import java.io.File;
import java.util.List;

/**
 * Interface for processing a list of directories.
 *
 * @author pcampbell
 */
interface SourceFileProvider {

    /**
     * Process the list of directories and return the list of java files.
     *
     * @param directories the directories to process
     */
    List<File> process(List<String> directories);

}

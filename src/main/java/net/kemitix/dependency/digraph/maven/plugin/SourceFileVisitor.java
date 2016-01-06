package net.kemitix.dependency.digraph.maven.plugin;

import java.io.File;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.util.List;

/**
 * File visitor to discover Java source files.
 *
 * @author pcampbell
 */
public interface SourceFileVisitor extends FileVisitor<Path>, MojoService {

    /**
     * Returns the list of Java source files found.
     *
     * @return the list of Java source files
     */
    List<File> getJavaFiles();

}

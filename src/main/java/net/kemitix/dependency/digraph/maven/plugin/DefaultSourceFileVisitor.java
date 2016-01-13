package net.kemitix.dependency.digraph.maven.plugin;

import lombok.Getter;
import org.apache.maven.plugin.logging.Log;

import javax.inject.Inject;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the source file visitor.
 *
 * @author pcampbell
 */
class DefaultSourceFileVisitor extends SimpleFileVisitor<Path>
        implements SourceFileVisitor {

    @Inject
    @Getter
    private DigraphMojo mojo;

    @Override
    public Log getLog() {
        return mojo.getLog();
    }

    /**
     * The list of Java files discovered.
     */
    @Getter
    private final List<File> javaFiles = new ArrayList<>();

    @Override
    public FileVisitResult visitFile(
            final Path file,
            final BasicFileAttributes attrs) throws IOException {
        if (file.toString().endsWith(".java")) {
            javaFiles.add(file.toFile());
        }
        return FileVisitResult.CONTINUE;
    }

}

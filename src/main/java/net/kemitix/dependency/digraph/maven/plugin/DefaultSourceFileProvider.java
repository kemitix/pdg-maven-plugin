package net.kemitix.dependency.digraph.maven.plugin;

import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provider walks the directory and builds a list of discovered Java files.
 *
 * @author pcampbell
 */
class DefaultSourceFileProvider extends AbstractMojoService
        implements SourceFileProvider {

    /**
     * The list of Java files discovered.
     */
    @Getter
    private final List<File> javaFiles = new ArrayList<>();

    @Override
    public void process(final List<String> directories) {
        final SourceFileVisitor fileVisitor = getMojo().getFileVisitor();
        directories.forEach((final String dir) -> {
            try {
                Files.walkFileTree(
                        new File(dir).getAbsoluteFile().toPath(), fileVisitor);
            } catch (IOException ex) {
                Logger.getLogger(DefaultSourceFileProvider.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        });
        javaFiles.addAll(fileVisitor.getJavaFiles());
    }

}

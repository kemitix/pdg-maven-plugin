package net.kemitix.dependency.digraph.maven.plugin;

import lombok.Getter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Processor to list directories to console.
 *
 * @author pcampbell
 */
class DirectoryListingProcessor extends AbstractMojoService
        implements SourceFileProvider {

    /**
     * The list of Java files discovered.
     *
     * <p>
     * Will always be empty in this implementation.
     */
    @Getter
    private final List<File> javaFiles = new ArrayList<>();

    @Override
    public void process(final List<String> directories) {
        directories.forEach((final String dir) -> {
            getMojo().getLog().info("* " + dir);
        });
    }

}

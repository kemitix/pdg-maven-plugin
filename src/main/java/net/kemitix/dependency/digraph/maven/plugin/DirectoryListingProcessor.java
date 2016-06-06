package net.kemitix.dependency.digraph.maven.plugin;

import org.apache.maven.plugin.logging.Log;

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

    @Override
    public List<File> process(final List<String> directories) {
        final Log log = getLog();
        directories.forEach((final String dir) -> {
            log.info("* " + dir);
        });
        return new ArrayList<>();
    }

}

package net.kemitix.dependency.digraph.maven.plugin;

import lombok.val;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Processor to list directories to console.
 *
 * @author pcampbell
 */
class DirectoryListingProcessor implements SourceFileProvider {

    private final DigraphMojo mojo;

    @Inject
    DirectoryListingProcessor(final DigraphMojo mojo) {
        this.mojo = mojo;
    }

    @Override
    public List<File> process(final List<String> directories) {
        val log = mojo.getLog();
        directories.stream().map(d -> "* " + d).forEach(log::info);
        return new ArrayList<>();
    }

}

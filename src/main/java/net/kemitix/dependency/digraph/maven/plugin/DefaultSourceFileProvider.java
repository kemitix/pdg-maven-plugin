package net.kemitix.dependency.digraph.maven.plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.inject.Inject;

/**
 * Provider walks the directory and builds a list of discovered Java files.
 *
 * @author pcampbell
 */
class DefaultSourceFileProvider implements SourceFileProvider {

    private final SourceFileVisitor fileVisitor;

    private final DigraphMojo mojo;

    @Inject
    DefaultSourceFileProvider(
            final SourceFileVisitor fileVisitor, final DigraphMojo mojo) {
        this.fileVisitor = fileVisitor;
        this.mojo = mojo;
    }

    @Override
    public List<File> process(final List<String> directories) {
        directories.forEach((final String dir) -> {
            try {
                Path start = new File(dir).getAbsoluteFile().toPath();
                Files.walkFileTree(start, fileVisitor);
            } catch (IOException ex) {
                mojo.getLog().error(ex);
            }
        });
        return fileVisitor.getJavaFiles();
    }

}

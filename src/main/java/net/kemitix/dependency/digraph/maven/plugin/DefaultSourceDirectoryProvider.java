package net.kemitix.dependency.digraph.maven.plugin;

import org.apache.maven.project.MavenProject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a provider for the list of source directories.
 *
 * @author pcampbell
 */
public class DefaultSourceDirectoryProvider implements SourceDirectoryProvider {

    @Override
    public List<String> getSourceDirectories(
            final List<MavenProject> projects) {
        final List<String> sourceDirectories = new ArrayList<>();
        projects.forEach((final MavenProject project) -> {
            final String directory = project.getBuild().getSourceDirectory();
            if (Files.isDirectory(Paths.get(directory))) {
                sourceDirectories.add(directory);
            }
        });
        return sourceDirectories;
    }

}

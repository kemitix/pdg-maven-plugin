package net.kemitix.dependency.digraph.maven.plugin;

import org.apache.maven.model.Build;
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
class DefaultSourceDirectoryProvider extends AbstractMojoService
        implements SourceDirectoryProvider {

    @Override
    public List<String> getDirectories(
            final List<MavenProject> projects, final boolean includeTests) {
        final List<String> directories = new ArrayList<>();
        projects.forEach((final MavenProject project) -> {
            addProject(directories, project, includeTests);
        });
        return directories;
    }

    private void addProject(
            final List<String> directories, final MavenProject project,
            final boolean includeTests) {
        final Build build = project.getBuild();
        addDirectoryIfExists(directories, build.getSourceDirectory());
        if (includeTests) {
            addDirectoryIfExists(directories, build.getTestSourceDirectory());
        }
    }

    private void addDirectoryIfExists(
            final List<String> directories, final String directory) {
        if (null != directory && Files.isDirectory(Paths.get(directory))) {
            directories.add(directory);
        }
    }

}

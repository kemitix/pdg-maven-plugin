package net.kemitix.dependency.digraph.maven.plugin;

import org.apache.maven.project.MavenProject;

import java.util.List;

/**
 * Provider for the list of source directories.
 *
 * @author pcampbell
 */
public interface SourceDirectoryProvider {

    /**
     * Returns all the source directories for the project, including any
     * modules.
     *
     * @param projects     the list of maven projects
     * @param includeTests whether to include test sources
     *
     * @return the list of source directories
     */
    List<String> getDirectories(
            final List<MavenProject> projects,
            final boolean includeTests);

}

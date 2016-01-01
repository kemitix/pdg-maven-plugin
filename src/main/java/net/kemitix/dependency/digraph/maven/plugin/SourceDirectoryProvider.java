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
     * @param projects the list of maven projects
     *
     * @return the list of source directories
     */
    List<String> getSourceDirectories(final List<MavenProject> projects);

}

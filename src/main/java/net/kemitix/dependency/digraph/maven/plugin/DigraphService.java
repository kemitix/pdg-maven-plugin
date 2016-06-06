package net.kemitix.dependency.digraph.maven.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;

import java.util.List;

/**
 * The Digraph Service.
 *
 * @author pcampbell
 */
interface DigraphService {

    /**
     * Executes the Digraph service.
     *
     * @param mojo         the parent mojo
     * @param projects     the project(s) to analyse
     * @param includeTests whether to include test code
     * @param basePackage  the base package to scan
     * @param format       the output format
     * @param debug        whether debug output should be included
     */
    void execute(
            AbstractMojo mojo, List<MavenProject> projects,
            boolean includeTests, String basePackage, String format,
            boolean debug);
}

package net.kemitix.dependency.digraph.maven.plugin;

import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.Getter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.util.List;

/**
 * Generates digraph.
 */
@Mojo(name = "digraph", aggregator = true)
public class DigraphMojo extends AbstractMojo {

    @Parameter(defaultValue = "${reactorProjects}", readonly = true)
    private List<MavenProject> projects;

    @Parameter(name = "includeTests", defaultValue = "true", required = false)
    private boolean includeTests;

    /**
     * Create Guice Injector.
     */
    private final Injector injector = Guice.createInjector(new DigraphModule());

    @Getter
    private List<String> directories;

    @Override
    public void execute() {
        directories = injector.getInstance(SourceDirectoryProvider.class)
                .getDirectories(projects, includeTests);
        listSourceDirectories(directories);
    }

    private void listSourceDirectories(final List<String> sourceDirectories) {
        sourceDirectories.forEach((final String dir) -> {
            getLog().info("* " + dir);
        });
    }

}

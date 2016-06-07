package net.kemitix.dependency.digraph.maven.plugin;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.NonNull;
import lombok.Setter;
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

    private final Injector injector;

    private final DigraphService digraphService;

    @Setter
    @Parameter(defaultValue = "${reactorProjects}", readonly = true)
    private List<MavenProject> projects;

    @Parameter(name = "includeTests", defaultValue = "false")
    private boolean includeTests;

    @NonNull
    @Setter
    @Parameter(name = "basePackage", required = true)
    private String basePackage;

    @Parameter(name = "debug", defaultValue = "true")
    private boolean debug;

    @Setter
    @Parameter(name = "format", defaultValue = "nested")
    private String format;

    /**
     * Default constructor.
     */
    public DigraphMojo() {
        injector = Guice.createInjector(new DigraphModule(),
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(DigraphMojo.class).toInstance(DigraphMojo.this);
                    }
                });
        digraphService = injector.getInstance(DigraphService.class);
    }

    @Override
    public void execute() {
        digraphService.execute(this, projects, includeTests, basePackage,
                format, debug);
    }

}

/*
The MIT License (MIT)

Copyright (c) 2016 Paul Campbell

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package net.kemitix.dependency.digraph.maven.plugin;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import lombok.Setter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.util.List;

/**
 * Generates digraph.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@Mojo(name = "digraph", aggregator = true)
public class DigraphMojo extends AbstractMojo {

    private final DigraphService digraphService;

    @Setter
    @Parameter(defaultValue = "${reactorProjects}", readonly = true)
    private List<MavenProject> projects;

    @Parameter(name = "includeTests", defaultValue = "false")
    private boolean includeTests;

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
        digraphService = Guice.createInjector(new DigraphModule(),
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(DigraphMojo.class).toInstance(DigraphMojo.this);
                    }
                }).getInstance(DigraphService.class);
    }

    @Override
    public final void execute() {
        digraphService.execute(this, projects, includeTests, basePackage,
                format, debug);
    }
}

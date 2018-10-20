/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Paul Campbell
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.kemitix.pdg.maven;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.List;

/**
 * Represents the configuration for the {@link DigraphMojo}.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
interface DigraphConfiguration {

    /**
     * The Maven Project.
     *
     * @return the Maven Project
     */
    public abstract MavenProject getProject();

    /**
     * All the Maven Projects in a multi-module project.
     *
     * @return a list of Maven Projects
     */
    public abstract List<MavenProject> getProjects();

    /**
     * The project build directory.
     *
     * @return the target directory
     */
    public abstract File getOutputDirectory();

    /**
     * Flag whether to include tests in graph.
     *
     * @return true if the tests should be included
     */
    public abstract boolean isIncludeTests();

    /**
     * The base package to analyse within.
     *
     * @return the name of the base pacakge
     */
    public abstract String getBasePackage();

    /**
     * Flag whether to log debug messages.
     *
     * @return true if the debug messages should be logged
     */
    public abstract boolean isDebug();

    /**
     * The format of the graph to create.
     *
     * @return the format name: nested of simple
     */
    public abstract String getFormat();

    /**
     * Access to the maven plugin logger.
     *
     * @return the Log interface
     */
    public abstract Log getLog();
}

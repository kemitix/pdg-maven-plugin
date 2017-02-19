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

import org.apache.maven.model.Build;
import org.apache.maven.project.MavenProject;

import javax.annotation.concurrent.Immutable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a provider for the list of source directories.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@Immutable
class DefaultSourceDirectoryProvider implements SourceDirectoryProvider {

    @Override
    public List<String> getDirectories(
            final List<MavenProject> projects, final boolean includeTests
                                      ) {
        final List<String> directories = new ArrayList<>();
        projects.forEach((final MavenProject project) -> {
            addProject(directories, project, includeTests);
        });
        return directories;
    }

    private void addProject(
            final List<String> directories, final MavenProject project, final boolean includeTests
                           ) {
        final Build build = project.getBuild();
        addDirectoryIfExists(directories, build.getSourceDirectory());
        if (includeTests) {
            addDirectoryIfExists(directories, build.getTestSourceDirectory());
        }
    }

    private void addDirectoryIfExists(
            final List<String> directories, final String directory
                                     ) {
        if (directory != null && Files.isDirectory(Paths.get(directory))) {
            directories.add(directory);
        }
    }

}

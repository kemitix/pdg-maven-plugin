package net.kemitix.pdg.maven;

import org.apache.maven.model.Build;
import org.apache.maven.project.MavenProject;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class DigraphMojoTest implements WithAssertions {

    private final MavenProject project = mock(MavenProject.class);
    private final Build build = mock(Build.class);
    private final DigraphMojo mojo = new DigraphMojo();

    @Test
    void shouldRunWithoutErrorWhenBasePackageProvided() {
        //given
        mojo.setProjects(new ArrayList<>());
        // set defaults
        mojo.setFormat("nested");
        mojo.setProject(project);
        given(project.getBuild()).willReturn(build);
        given(build.getDirectory()).willReturn("target");
        mojo.setBasePackage("net.kemitix");
        //when
        assertThatCode(mojo::execute).doesNotThrowAnyException();
    }
}

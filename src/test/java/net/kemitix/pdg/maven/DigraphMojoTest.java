package net.kemitix.pdg.maven;

import org.apache.maven.model.Build;
import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.mockito.BDDMockito.given;

/**
 * Tests for {@link DigraphMojo}.
 *
 * @author pcampbell
 */
public class DigraphMojoTest {

    private DigraphMojo mojo;

    @Mock
    private MavenProject project;

    @Mock
    private Build build;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mojo = new DigraphMojo();
        mojo.setProjects(new ArrayList<>());
        // set defaults
        mojo.setFormat("nested");
        mojo.setProject(project);
        given(project.getBuild()).willReturn(build);
        given(build.getDirectory()).willReturn("target");
    }

    @Test
    public void shouldRunWithoutErrorWhenBasePackageProvided() {
        //given
        mojo.setBasePackage("net.kemitix");
        //when
        mojo.execute();
    }
}

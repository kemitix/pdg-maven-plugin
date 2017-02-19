package net.kemitix.dependency.digraph.maven.plugin;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Tests for {@link DigraphMojo}.
 *
 * @author pcampbell
 */
public class DigraphMojoTest {

    private DigraphMojo mojo;

    @Before
    public void setUp() throws Exception {
        mojo = new DigraphMojo();
        mojo.setProjects(new ArrayList<>());
        // set defaults
        mojo.setFormat("nested");
    }

    @Test
    public void shouldRunWithoutErrorWhenBasePackageProvided() {
        //given
        mojo.setBasePackage("net.kemitix");
        //when
        mojo.execute();
    }
}

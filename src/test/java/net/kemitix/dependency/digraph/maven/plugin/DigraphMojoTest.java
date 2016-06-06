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
    }

    @Test
    public void execute() {
        //given
        mojo.setProjects(new ArrayList<>());
        mojo.setBasePackage("net.kemitix");
        mojo.setFormat("nested");
        //when
        mojo.execute();
    }

}

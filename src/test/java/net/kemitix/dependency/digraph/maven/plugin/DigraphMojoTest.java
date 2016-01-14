package net.kemitix.dependency.digraph.maven.plugin;

import org.apache.maven.plugin.testing.MojoRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link DigraphMojo}.
 *
 * @author pcampbell
 */
public class DigraphMojoTest {

    @Rule
    public final MojoRule rule = new MojoRule();

    /**
     * Test that source and test directories are detected.
     *
     * @throws java.lang.Exception if error opening pom.xml
     */
    @Test
    public void shouldListSrcAndTestDirectories() throws Exception {
        //given
        final String goal = "digraph";
        final String pom = "src/test/projects/src-and-test/pom.xml";
        final String baseDir = new File(pom).getParentFile().getAbsolutePath();
        DigraphMojo mojo = (DigraphMojo) rule.lookupMojo(goal, pom);
        mojo.setFormat("nested");
        assertNotNull(mojo);
        //when
        mojo.execute();
        //then
        List<String> directories = mojo.getDirectories();
        assertThat(directories.size(), is(2));
        assertTrue(directories.contains(baseDir + "/src/main/java"));
        assertTrue(directories.contains(baseDir + "/src/test/java"));
    }

    /**
     * Test that source directories are detected and missing tests are not.
     *
     * @throws java.lang.Exception if error opening pom.xml
     */
    @Test
    public void shouldListSrcOnlyDirectories() throws Exception {
        //given
        final String goal = "digraph";
        final String pom = "src/test/projects/src-only/pom.xml";
        final String baseDir = new File(pom).getParentFile().getAbsolutePath();
        final DigraphMojo mojo = (DigraphMojo) rule.lookupMojo(goal, pom);
        mojo.setFormat("nested");
        assertNotNull(mojo);
        //when
        mojo.execute();
        //then
        List<String> directories = mojo.getDirectories();
        assertThat(directories.size(), is(1));
        assertTrue(directories.contains(baseDir + "/src/main/java"));
    }
}

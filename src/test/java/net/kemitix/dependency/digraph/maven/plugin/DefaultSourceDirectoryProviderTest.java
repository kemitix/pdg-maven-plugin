package net.kemitix.dependency.digraph.maven.plugin;

import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.project.MavenProject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link DefaultSourceDirectoryProviderTest}.
 *
 * @author pcampbell
 */
public class DefaultSourceDirectoryProviderTest {

    /**
     * Class under test.
     */
    private DefaultSourceDirectoryProvider provider;

    /**
     * Project list.
     */
    private final List<MavenProject> projects = new ArrayList<>();

    /**
     * Build.
     */
    private final Build build = new Build();

    /**
     * Temporary Files and Directories.
     */
    private final TemporaryFileManager fileManager
            = new TemporaryFileManager();

    /**
     * Prepare each test.
     */
    @Before
    public void setUp() {
        provider = new DefaultSourceDirectoryProvider();
        final Model model = new Model();
        model.setBuild(build);
        projects.add(new MavenProject(model));
    }

    /**
     * Clean up after each test.
     */
    @After
    public void tearDown() {
        fileManager.cleanUp();
    }

    /**
     * should be able to get source directory out after setting it.
     *
     * @throws java.io.IOException if error creating directory
     */
    @Test
    public void shouldGetSourceDirectoryAfterSettingIt() throws IOException {
        //given
        final String directory = fileManager.createTempDirectory().toString();
        //when
        build.setSourceDirectory(directory);
        //then
        assertThat(projects.get(0).getModel().getBuild().getSourceDirectory(),
                is(directory));
    }

    /**
     * add project - src exists - with tests - test exists.
     *
     * @throws java.io.IOException if error creating directory
     */
    @Test
    public void shouldAddSourcesAndTests() throws IOException {
        //given
        final String sources = fileManager.createTempDirectory().toString();
        build.setSourceDirectory(sources);
        final String tests = fileManager.createTempDirectory().toString();
        build.setTestSourceDirectory(tests);
        //when
        List<String> directories = provider.getDirectories(projects, true);
        //then
        assertThat(directories.size(), is(2));
        assertTrue(directories.contains(sources));
        assertTrue(directories.contains(tests));
    }

    /**
     * add project - src exists - with tests - test do not exists.
     *
     * @throws java.io.IOException if error creating directory
     */
    @Test
    public void shouldAddSourcesAndNoTestsWhenTestsDoNotExist()
            throws IOException {
        //given
        final String sources = fileManager.createTempDirectory().toString();
        build.setSourceDirectory(sources);
        final String tests = "do not exist";
        build.setTestSourceDirectory(tests);
        //when
        List<String> directories = provider.getDirectories(projects, true);
        //then
        assertThat(directories.size(), is(1));
        assertTrue(directories.contains(sources));
        assertFalse(directories.contains(tests));
    }

    /**
     * add project - src exists - without tests.
     *
     * @throws java.io.IOException if error creating directory
     */
    @Test
    public void shouldAddSourcesAndNoTests() throws IOException {
        //given
        final String sources = fileManager.createTempDirectory().toString();
        build.setSourceDirectory(sources);
        //when
        List<String> directories = provider.getDirectories(projects, true);
        //then
        assertThat(directories.size(), is(1));
        assertTrue(directories.contains(sources));
    }

    /**
     * add project - src does not exists - without tests.
     */
    @Test
    public void shouldNotAddAnySources() {
        //given
        final String sources = "does not exist";
        build.setSourceDirectory(sources);
        //when
        List<String> directories = provider.getDirectories(projects, true);
        //then
        assertThat(directories.size(), is(0));
        assertFalse(directories.contains(sources));
    }

}

package net.kemitix.pdg.maven;

import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.project.MavenProject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DefaultSourceDirectoryProviderTest}.
 *
 * @author pcampbell
 */
public class DefaultSourceDirectoryProviderTest {

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
    private final TemporaryFileManager fileManager = new TemporaryFileManager();

    /**
     * Class under test.
     */
    private DefaultSourceDirectoryProvider provider;

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
        final String directory = fileManager.createTempDirectory()
                                            .toString();
        //when
        build.setSourceDirectory(directory);
        //then
        final String sourceDirectory = projects.get(0)
                                               .getModel()
                                               .getBuild()
                                               .getSourceDirectory();
        assertThat(sourceDirectory).isEqualTo(directory);
    }

    /**
     * add project - src exists - with tests - test exists.
     *
     * @throws java.io.IOException if error creating directory
     */
    @Test
    public void shouldAddSourcesAndTests() throws IOException {
        //given
        final String sources = fileManager.createTempDirectory()
                                          .toString();
        build.setSourceDirectory(sources);
        final String tests = fileManager.createTempDirectory()
                                        .toString();
        build.setTestSourceDirectory(tests);
        //when
        List<String> directories = provider.getDirectories(projects, true);
        //then
        assertThat(directories).containsExactly(sources, tests);
    }

    /**
     * add project - src exists - with tests - test do not exists.
     *
     * @throws java.io.IOException if error creating directory
     */
    @Test
    public void shouldAddSourcesAndNoTestsWhenTestsDoNotExist() throws IOException {
        //given
        final String sources = fileManager.createTempDirectory()
                                          .toString();
        build.setSourceDirectory(sources);
        final String tests = "do not exist";
        build.setTestSourceDirectory(tests);
        //when
        List<String> directories = provider.getDirectories(projects, true);
        //then
        assertThat(directories).containsExactly(sources)
                               .doesNotContain(tests);
    }

    /**
     * add project - src exists - without tests.
     *
     * @throws java.io.IOException if error creating directory
     */
    @Test
    public void shouldAddSourcesAndNoTests() throws IOException {
        //given
        final String sources = fileManager.createTempDirectory()
                                          .toString();
        build.setSourceDirectory(sources);
        //when
        List<String> directories = provider.getDirectories(projects, true);
        //then
        assertThat(directories).containsExactly(sources);
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
        assertThat(directories).isEmpty();
    }
}

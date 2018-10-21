package net.kemitix.pdg.maven;

import lombok.val;
import org.apache.maven.model.Build;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link DigraphService}.
 *
 * @author pcampbell
 */
@EnableRuleMigrationSupport
public class DefaultDigraphServiceTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    // collaborators
    private final PackageScanner packageScanner = mock(PackageScanner.class);
    private final ReportGenerator reportGenerator = mock(ReportGenerator.class);
    private final ReportWriter reportWriter = mock(ReportWriter.class);
    private final DotFileFormatFactory dotFileFormatFactory = mock(DotFileFormatFactory.class);

    // subject under test
    private final DefaultDigraphService digraphService =
            new DefaultDigraphService(packageScanner, reportGenerator, reportWriter, dotFileFormatFactory);

    // parameters
    private final List<MavenProject> mavenProjects = new ArrayList<>();
    private final DigraphConfiguration configuration = mock(DigraphConfiguration.class);
    private final MavenProject project = mock(MavenProject.class);
    private final Build build = mock(Build.class);
    private final DependencyData dependencyData = mock(DependencyData.class);
    private final Log log = mock(Log.class);

    @BeforeEach
    public void setUp() {
        given(configuration.getLog()).willReturn(log);
        given(configuration.getBasePackage()).willReturn("net.kemitix");
        given(configuration.getProjects()).willReturn(mavenProjects);
        given(configuration.isIncludeTests()).willReturn(false);
        given(configuration.isDebug()).willReturn(false);
        given(configuration.getProject()).willReturn(project);
        given(project.getBuild()).willReturn(build);
        given(build.getDirectory()).willReturn("target");
        given(packageScanner.scan(configuration)).willReturn(dependencyData);
    }

    @Test
    public void whenExecuteThenPackagesAreScanned() {
        //when
        digraphService.execute(configuration);
        //then
        then(packageScanner).should().scan(configuration);
    }

    @Test
    public void whenIsDebugThenLogIsRequested() {
        //given
        given(configuration.isDebug()).willReturn(true);
        //when
        digraphService.execute(configuration);
        //then
        then(configuration).should().getLog();
    }

    @Test
    public void whenIOExceptionThenLogAnError() throws Exception {
        //given
        String message = "message";
        doThrow(new IOException(message)).when(reportWriter)
                                         .write(any(), any());
        //when
        digraphService.execute(configuration);
        //then
        then(log).should().error(message);
    }

    @Test
    public void whenTargetDirectoryDoesNotExistThenCreateIt() throws Exception {
        //given
        val target = folder.newFolder();
        target.delete();
        assertThat(target).doesNotExist();
        given(build.getDirectory()).willReturn(target.getAbsolutePath());
        //when
        digraphService.execute(configuration);
        //then
        assertThat(target).exists()
                          .isDirectory();
    }
}

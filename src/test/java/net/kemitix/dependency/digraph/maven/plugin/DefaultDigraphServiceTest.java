package net.kemitix.dependency.digraph.maven.plugin;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Tests for {@link DigraphService}.
 *
 * @author pcampbell
 */
public class DefaultDigraphServiceTest {

    @InjectMocks
    private DefaultDigraphService digraphService;

    @Mock
    private DigraphMojo digraphMojo;

    @Mock
    private SourceDirectoryProvider directoryProvider;

    @Mock
    private SourceFileProvider fileProvider;

    @Mock
    private FileLoader fileLoader;

    @Mock
    private SourceFileAnalyser fileAnalyser;

    @Mock
    private DependencyData dependencyData;

    @Mock
    private ReportGenerator reportGenerator;

    @Mock
    private ReportWriter reportWriter;

    @Mock
    private NodePathGenerator nodePathGenerator;

    @Mock
    private InputStream inputStream;

    @Mock
    private DotFileFormatFactory dotFileFormatFactory;

    @Mock
    private Log log;

    private List<MavenProject> mavenProjects;

    private boolean includeTests;

    private String basePackage;

    private String format;

    private boolean debug;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        given(digraphMojo.getLog()).willReturn(log);
    }

    @Test
    public void execute() {
        //when
        digraphService.execute(digraphMojo, mavenProjects, includeTests,
                basePackage, format, debug);
    }

    @Test
    public void executeWithDebug() {
        //given
        debug = true;
        //when
        digraphService.execute(digraphMojo, mavenProjects, includeTests,
                basePackage, format, debug);
        //then
        verify(dependencyData).debugLog(log);
    }

    @Test
    public void executeWithIOException() throws Exception {
        //given
        String message = "message";
        doThrow(new IOException(message)).when(reportWriter)
                                         .write(anyString(), anyString());
        //when
        digraphService.execute(digraphMojo, mavenProjects, includeTests,
                basePackage, format, debug);
        //then
        verify(log).error(message);
    }
}

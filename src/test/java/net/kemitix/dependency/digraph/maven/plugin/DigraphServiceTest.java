package net.kemitix.dependency.digraph.maven.plugin;

import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.InputStream;
import java.util.List;

/**
 * Tests for {@link DigraphService}.
 *
 * @author pcampbell
 */
public class DigraphServiceTest {

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

    private List<MavenProject> mavenProjects;

    private boolean includeTests;

    private String basePackage;

    private String format;

    private boolean debug;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void execute() {
        //when
        digraphService.execute(digraphMojo, mavenProjects, includeTests,
                basePackage, format, debug);
    }
}

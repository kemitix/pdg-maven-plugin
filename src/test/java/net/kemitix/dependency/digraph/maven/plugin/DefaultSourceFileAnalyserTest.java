package net.kemitix.dependency.digraph.maven.plugin;

import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.File;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link DefaultSourceFileAnalyser}.
 *
 * @author pcampbell
 */
public class DefaultSourceFileAnalyserTest {

    /**
     * Class under test.
     */
    @InjectMocks
    private DefaultSourceFileAnalyser analyser;

    @Mock
    private DependencyData dependencyData;

    @Mock
    private DigraphMojo mojo;

    @Mock
    private Log log;

    private static final String SRC_JAVA
            = "src/test/projects/src-and-test/"
            + "src/main/java/test/nested/Src.java";

    /**
     * Prepare each test.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        doReturn(log).when(mojo).getLog();
        doAnswer((Answer) (InvocationOnMock invocation) -> {
            System.out.println(invocation.getArgumentAt(0, String.class));
            return null;
        }).when(log).info(any(String.class));
    }

    /**
     * Should parse Src.java file.
     */
    @Test
    @SuppressWarnings("magicnumber")
    public void shouldParseSrcFile() {
        //given
        File file = new File(SRC_JAVA);
        //when
        analyser.analyse(file);
        //then
        verify(dependencyData, times(3))
                .addDependency("test.nested", "test.other");
    }

    /**
     * Should handle IOException.
     */
    @Test
    public void shouldSwallowIOException() {
        //given
        File file = new File("I do not exist");
        //when
        analyser.analyse(file);
        //then
        // IOException is not thrown
    }

    /**
     * Should handle invalid file content.
     */
    @Test
    public void shouldSwallowParseException() {
        //given
        File file = new File("src/test/projects/src-and-test/pom.xml");
        //when
        analyser.analyse(file);
        //then
        // ParseException is not thrown
    }

}

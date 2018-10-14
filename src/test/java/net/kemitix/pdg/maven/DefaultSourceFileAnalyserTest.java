package net.kemitix.pdg.maven;

import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.doAnswer;
import static org.mockito.BDDMockito.doReturn;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

/**
 * Tests for {@link DefaultSourceFileAnalyser}.
 *
 * @author pcampbell
 */
public class DefaultSourceFileAnalyserTest {

    private DefaultSourceFileAnalyser analyser;

    @Mock
    private DependencyData dependencyData;

    @Mock
    private DigraphMojo mojo;

    @Mock
    private Log log;

    /**
     * Prepare each test.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        analyser = new DefaultSourceFileAnalyser(mojo);
        doReturn(log).when(mojo)
                     .getLog();
        doAnswer((InvocationOnMock invocation) -> {
            return null;
        }).when(log)
          .info(any(String.class));
    }

    /**
     * Should parse Src.java file.
     */
    @Test
    @SuppressWarnings("magicnumber")
    public void shouldParseSrcFile() {
        //given
        String srcJava =
                "package test.nested;\n" + "import test.other.Imported;\n" + "import static test.other.Static.method;\n"
                + "import static test.other.StaticAll.*;\n" + "public class Src {}";
        InputStream stream = new ByteArrayInputStream(srcJava.getBytes(UTF_8));
        //when
        analyser.analyse(dependencyData, stream);
        //then
        then(dependencyData).should(times(3))
                            .addDependency("test.nested", "test.other");
    }

    /**
     * Should handle invalid file content.
     */
    @Test
    public void shouldSwallowParseException() {
        //given
        String nonJava = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root/>";
        InputStream stream = new ByteArrayInputStream(nonJava.getBytes(UTF_8));
        //when
        analyser.analyse(dependencyData, stream);
        //then
        // ParseException is not thrown
    }

    /**
     * Should handle commented file content.
     */
    @Test
    public void shouldHandleCommentedFileContent() {
        //given
        String srcJava = "//package test.nested;\n" + "//import test.other.Imported;\n"
                         + "//import static test.other.Static.method;\n" + "//import static test.other.StaticAll.*;\n"
                         + "//public class Src {}";
        InputStream stream = new ByteArrayInputStream(srcJava.getBytes(UTF_8));
        //when
        analyser.analyse(dependencyData, stream);
        //then
        // ParseException is not thrown
    }

}

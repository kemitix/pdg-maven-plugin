package net.kemitix.pdg.maven.scan;

import net.kemitix.pdg.maven.DependencyData;
import net.kemitix.pdg.maven.DigraphConfiguration;
import org.apache.maven.plugin.logging.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

/**
 * Tests for {@link JavaParserSourceFileAnalyser}.
 *
 * @author pcampbell
 */
public class JavaParserSourceFileAnalyserTest {

    private final DependencyData dependencyData = mock(DependencyData.class);
    private final DigraphConfiguration digraphConfiguration = mock(DigraphConfiguration.class);
    private final Log log = mock(Log.class);

    private final JavaParserSourceFileAnalyser analyser = new JavaParserSourceFileAnalyser(digraphConfiguration);

    /**
     * Prepare each test.
     */
    @BeforeEach
    public void setUp() {
        doReturn(log).when(digraphConfiguration).getLog();
        doAnswer(invocation -> null).when(log).info(any(String.class));
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

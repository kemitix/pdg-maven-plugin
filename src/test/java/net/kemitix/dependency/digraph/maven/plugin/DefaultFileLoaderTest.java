package net.kemitix.dependency.digraph.maven.plugin;

import com.google.common.io.Files;
import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;

/**
 * Tests for {@link DefaultFileLoader}.
 *
 * @author pcampbell
 */
public class DefaultFileLoaderTest {

    @InjectMocks
    private DefaultFileLoader fileLoader;

    @Mock
    private DigraphMojo mojo;

    @Mock
    private Log log;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        doReturn(log).when(mojo).getLog();
    }

    @Test
    public void shouldLoadFile() throws IOException {
        //given
        File file = new File("src/test/projects/src-only/pom.xml");
        String expectedFirstLine = Files.readFirstLine(file, UTF_8);
        //when
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                fileLoader.asInputStream(file), UTF_8));
        String streamedFirstLine = reader.readLine();
        //then
        assertThat(streamedFirstLine, is(expectedFirstLine));
    }

    @Test
    public void shouldReturnNullWhenFileNotFound() {
        //given
        File file = new File("src/test/projects/imaginary/pom.xml");
        //when
        InputStream stream = fileLoader.asInputStream(file);
        //then
        assertThat(stream, is(nullValue()));
    }

}

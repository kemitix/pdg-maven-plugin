package net.kemitix.dependency.digraph.maven.plugin;

import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.doReturn;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.stream.Collectors;

/**
 * Tests for {@link DefaultFileLoader}.
 *
 * @author pcampbell
 */
public class DefaultFileLoaderTest {

    private DefaultFileLoader fileLoader;

    @Mock
    private DigraphMojo mojo;

    @Mock
    private Log log;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        fileLoader = new DefaultFileLoader(mojo);
        doReturn(log).when(mojo).getLog();
    }

    @Test
    public void shouldLoadFile() throws IOException {
        //given
        File file = new File("src/test/projects/src-only/pom.xml");
        String expectedFirstLine = Files.lines(file.toPath(), StandardCharsets.UTF_8)
                         .limit(1)
                         .collect(Collectors.joining());
        //when
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(fileLoader.asInputStream(file),
                        StandardCharsets.UTF_8));
        String streamedFirstLine = reader.readLine();
        //then
        assertThat(streamedFirstLine).isEqualTo(expectedFirstLine);
    }

    @Test
    public void shouldReturnNullWhenFileNotFound() {
        //given
        File file = new File("src/test/projects/imaginary/pom.xml");
        //when
        InputStream stream = fileLoader.asInputStream(file);
        //then
        assertThat(stream).isNull();
    }
}

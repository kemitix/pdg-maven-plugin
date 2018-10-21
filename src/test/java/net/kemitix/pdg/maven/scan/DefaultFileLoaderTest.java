package net.kemitix.pdg.maven.scan;

import net.kemitix.pdg.maven.DigraphConfiguration;
import org.apache.maven.plugin.logging.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link DefaultFileLoader}.
 *
 * @author pcampbell
 */
public class DefaultFileLoaderTest {

    private final DigraphConfiguration digraphConfiguration = mock(DigraphConfiguration.class);
    private final Log log = mock(Log.class);

    private final DefaultFileLoader fileLoader = new DefaultFileLoader(digraphConfiguration);

    @BeforeEach
    public void setUp() {
        doReturn(log).when(digraphConfiguration).getLog();
    }

    @Test
    public void shouldLoadFile() throws IOException {
        //given
        final File file = new File("src/test/projects/src-only/pom.xml");
        final String expectedFirstLine = Files.lines(file.toPath(), StandardCharsets.UTF_8)
                .limit(1)
                .collect(Collectors.joining());
        //when
        final BufferedReader reader =
                new BufferedReader(new InputStreamReader(fileLoader.asInputStream(file), StandardCharsets.UTF_8));
        final String streamedFirstLine = reader.readLine();
        //then
        assertThat(streamedFirstLine).isEqualTo(expectedFirstLine);
    }

    @Test
    public void shouldReturnNullWhenFileNotFound() {
        //given
        final File file = new File("src/test/projects/imaginary/pom.xml");
        //when
        final InputStream stream = fileLoader.asInputStream(file);
        //then
        assertThat(stream).isNull();
    }
}

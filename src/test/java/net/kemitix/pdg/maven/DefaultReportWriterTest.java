package net.kemitix.pdg.maven;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

public class DefaultReportWriterTest {

    private DefaultReportWriter reportWriter = new DefaultReportWriter();

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    /**
     * Test to write report normally.
     *
     * @throws java.io.IOException if error creating file
     */
    @Test
    public void shouldWriteReport() throws IOException {
        //given
        final String report = "the report";
        final File file = folder.newFile();
        //when
        reportWriter.write(report, file.toString());
        //then
        List<String> content = Files.readAllLines(file.toPath(), UTF_8);
        assertThat(content).containsExactly(report);
    }

    /**
     * Test to write report with exception.
     *
     * @throws java.io.IOException if error creating file not caught
     */
    @Test(expected = IOException.class)
    public void shouldIgnoreErrorWritingReport() throws IOException {
        //given
        final String report = "the report";
        final File file = new File("/do/not/create");
        //when
        reportWriter.write(report, file.toString());
        //then
        // no exception is thrown
    }
}

package net.kemitix.pdg.maven;

import org.assertj.core.api.WithAssertions;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

@EnableRuleMigrationSupport
public class DefaultReportWriterTest implements WithAssertions {

    private final DefaultReportWriter reportWriter = new DefaultReportWriter();

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    /**
     * Test to write report normally.
     *
     * @throws java.io.IOException if error creating file
     */
    @Test
    void shouldWriteReport() throws IOException {
        //given
        final String report = "the report";
        final File file = folder.newFile();
        //when
        reportWriter.write(report, file.toString());
        //then
        final List<String> content = Files.readAllLines(file.toPath(), UTF_8);
        assertThat(content).containsExactly(report);
    }

    /**
     * Test to write report with exception.
     *
     * @throws java.io.IOException if error creating file not caught
     */
    @Test
    void shouldIgnoreErrorWritingReport() throws IOException {
        //given
        final String report = "the report";
        final File file = new File("/do/not/create");
        //then
        assertThatCode(() -> reportWriter.write(report, file.toString()))
                .isInstanceOf(IOException.class);
    }
}

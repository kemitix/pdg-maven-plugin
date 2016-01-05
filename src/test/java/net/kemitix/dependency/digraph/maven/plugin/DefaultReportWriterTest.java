package net.kemitix.dependency.digraph.maven.plugin;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link DefaultReportWriter}.
 *
 * @author pcampbell
 */
public class DefaultReportWriterTest {

    /**
     * Class under test.
     */
    private DefaultReportWriter reportWriter;

    private TemporaryFileManager fileManager;

    /**
     * Prepare each test.
     */
    @Before
    public void setUp() {
        reportWriter = new DefaultReportWriter();
        fileManager = new TemporaryFileManager();
    }

    /**
     * Clean up after each test.
     */
    @After
    public void tearDown() {
        fileManager.cleanUp();
    }

    /**
     * Test to write report normally.
     *
     * @throws java.io.IOException if error creating file
     */
    @Test
    public void shouldWriteReport() throws IOException {
        //given
        final String report = "the report";
        final File file = fileManager.createTempFile();
        //when
        reportWriter.write(report, file.toString());
        //then
        List<String> content = Files.readAllLines(file.toPath(), UTF_8);
        assertThat(content.size(), is(1));
        assertEquals(report, content.get(0));
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

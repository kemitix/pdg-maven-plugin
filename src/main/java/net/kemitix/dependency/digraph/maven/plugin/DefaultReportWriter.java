package net.kemitix.dependency.digraph.maven.plugin;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Writer for the report.
 *
 * @author pcampbell
 */
public class DefaultReportWriter extends AbstractMojoService
        implements ReportWriter {

    @Override
    public void write(final String report, final String file)
            throws IOException {
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(file), UTF_8);
            writer.append(report);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

}

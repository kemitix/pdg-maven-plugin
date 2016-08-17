package net.kemitix.dependency.digraph.maven.plugin;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

/**
 * Writer for the report.
 *
 * @author pcampbell
 */
class DefaultReportWriter implements ReportWriter {

    @Override
    public void write(final String report, final String file)
            throws IOException {
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(file),
                    StandardCharsets.UTF_8);
            writer.append(report);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

}

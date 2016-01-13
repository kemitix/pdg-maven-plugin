package net.kemitix.dependency.digraph.maven.plugin;

import java.io.IOException;

/**
 * Interface for writing the report.
 *
 * @author pcampbell
 */
interface ReportWriter extends MojoService {

    /**
     * Write the report.
     *
     * @param report the report to write
     * @param file   the file to write to
     *
     * @throws IOException if there is an error writing the report
     */
    void write(String report, String file) throws IOException;

}

package net.kemitix.dependency.digraph.maven.plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Manager for the creation and cleanup of temporary directories.
 *
 * @author pcampbell
 *
 * @deprecated see {@link org.junit.rules.TemporaryFolder}
 */
@Deprecated
public class TemporaryFileManager {

    private final Deque<File> createdFiles = new ArrayDeque<>();

    /**
     * Delete all the created files.
     */
    public void cleanUp() {
        createdFiles.forEach(File::delete);
    }

    /**
     * Creates a temporary directory.
     *
     * @return the created directory
     *
     * @throws IOException if error creating directory
     */
    public File createTempDirectory() throws IOException {
        File dir = Files.createTempDirectory("")
                        .toFile();
        createdFiles.push(dir);
        return dir;
    }

    /**
     * Creates a temporary file.
     *
     * @return the created file
     *
     * @throws IOException if error creating file
     */
    public File createTempFile() throws IOException {
        File file = Files.createTempFile("", "")
                         .toFile();
        createdFiles.push(file);
        return file;
    }

}

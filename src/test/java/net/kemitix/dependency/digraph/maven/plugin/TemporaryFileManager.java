package net.kemitix.dependency.digraph.maven.plugin;

import com.google.common.io.Files;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Manager for the creation and cleanup of temporary directories.
 *
 * @author pcampbell
 */
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
     */
    public File createTempDirectory() {
        File dir = Files.createTempDir();
        createdFiles.push(dir);
        return dir;
    }

}

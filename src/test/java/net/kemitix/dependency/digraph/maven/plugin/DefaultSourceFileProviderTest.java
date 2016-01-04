package net.kemitix.dependency.digraph.maven.plugin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

/**
 * Tests for {@link DefaultSourceFileProvider}.
 *
 * @author pcampbell
 */
public class DefaultSourceFileProviderTest {

    /**
     * Class under test.
     */
    @InjectMocks
    private DefaultSourceFileProvider sourceFileProvider;

    /**
     * Mock mojo.
     */
    @Mock
    private DigraphMojo mojo;

    /**
     * SourceFileVisitor.
     */
    private final SourceFileVisitor fileVisitor
            = new DefaultSourceFileVisitor();

    /**
     * Prepare each test.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sourceFileProvider.setMojo(mojo);
        doReturn(fileVisitor).when(mojo).getFileVisitor();
    }

    /**
     * Should attempt to walk each directory provided.
     *
     * @throws java.io.IOException if error walking files
     */
    @Test
    public void shouldLogEachDirectory() throws IOException {
        //given
        final String src = "src/test/projects/src-and-test/src/main/java";
        final String test = "src/test/projects/src-and-test/src/test/java";
        final List<String> directories = new ArrayList<>();
        directories.add(src);
        directories.add(test);
        //when
        sourceFileProvider.process(directories);
        //then
        List<File> javaFiles = fileVisitor.getJavaFiles();
        assertThat(javaFiles.size(), is(2));
        assertTrue(javaFiles.get(0).toString().contains(src));
        assertTrue(javaFiles.get(1).toString().contains(test));
    }

}

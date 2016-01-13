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
import static org.hamcrest.CoreMatchers.containsString;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
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
        javaFiles.sort(File::compareTo);
        /** 
         * The files are sorted so should appear in the order: 
         * main/.../nested/package-info.java, main/.../nested/Src.java, 
         * main/.../other/Imported.java, main/.../other/Static.java, 
         * main/.../other/StaticAll.java and test/.../Tst.java
         */
        final int numberOfClassFiles = 6;
        assertThat(javaFiles.size(), is(numberOfClassFiles));
        final String fileSeparator = System.getProperty("file.separator");
        assertThat(javaFiles.get(0).toString(), 
                containsString(src.replace("/", fileSeparator)));
        final int indexOfTestFile = 5;
        assertThat(javaFiles.get(indexOfTestFile).toString(), 
                containsString(test.replace("/", fileSeparator)));
    }

}

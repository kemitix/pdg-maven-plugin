package net.kemitix.dependency.digraph.maven.plugin;

import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests for {@link DefaultSourceFileProvider}.
 *
 * @author pcampbell
 */
public class DefaultSourceFileProviderTest {

    @InjectMocks
    private DefaultSourceFileProvider sourceFileProvider;

    @Mock
    private DigraphMojo mojo;

    @Mock
    private SourceFileVisitor fileVisitor;

    @Mock
    private Log log;

    private List<String> directories;

    private List<File> fileList;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        directories = new ArrayList<>();
        fileList = new ArrayList<>();
    }

    @Test
    public void processShouldReturnDirectories() throws Exception {
        //given
        final String src = "src/test/projects/src-and-test/src/main/java";
        final String test = "src/test/projects/src-and-test/src/test/java";
        directories.add(src);
        directories.add(test);
        given(fileVisitor.preVisitDirectory(any(), any())).willReturn(
                FileVisitResult.CONTINUE);
        given(fileVisitor.visitFile(any(), any())).willReturn(
                FileVisitResult.CONTINUE);
        given(fileVisitor.postVisitDirectory(any(), any())).willReturn(
                FileVisitResult.CONTINUE);
        given(fileVisitor.getJavaFiles()).willReturn(fileList);
        //when
        List<File> files = sourceFileProvider.process(directories);
        //then
        verify(fileVisitor).visitFile(
                eq(Paths.get(src, "test/nested/package-info.java")
                        .toAbsolutePath()), any());
        verify(fileVisitor).visitFile(
                eq(Paths.get(src, "test/nested/Src.java").toAbsolutePath()),
                any());
        verify(fileVisitor).visitFile(
                eq(Paths.get(src, "test/other/Imported.java").toAbsolutePath()),
                any());
        verify(fileVisitor).visitFile(
                eq(Paths.get(src, "test/other/Static.java").toAbsolutePath()),
                any());
        verify(fileVisitor).visitFile(
                eq(Paths.get(src, "test/other/StaticAll.java")
                        .toAbsolutePath()), any());
        verify(fileVisitor).visitFile(
                eq(Paths.get(test, "ignored.properties").toAbsolutePath()),
                any());
        verify(fileVisitor).visitFile(
                eq(Paths.get(test, "test/Tst.java").toAbsolutePath()), any());
        assertThat(files, is(fileList));
    }

    @Test
    public void processShouldLogExceptions() throws Exception {
        //given
        final String src = "src/test/projects/src-and-test/src/main/java";
        directories.add(src);
        IOException ex = new IOException();
        doThrow(ex).when(fileVisitor).preVisitDirectory(any(), any());
        given(mojo.getLog()).willReturn(log);
        //when
        sourceFileProvider.process(directories);
        //then
        verify(mojo.getLog()).error(ex);
    }

}

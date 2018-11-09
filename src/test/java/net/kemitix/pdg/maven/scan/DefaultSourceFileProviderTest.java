package net.kemitix.pdg.maven.scan;

import net.kemitix.pdg.maven.DigraphConfiguration;
import org.apache.maven.plugin.logging.Log;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

class DefaultSourceFileProviderTest implements WithAssertions {

    private final DigraphConfiguration digraphConfiguration = mock(DigraphConfiguration.class);
    private final SourceFileVisitor fileVisitor = mock(SourceFileVisitor.class);
    private final Log log = mock(Log.class);

    private final DefaultSourceFileProvider sourceFileProvider =
            new DefaultSourceFileProvider(fileVisitor, digraphConfiguration);

    private final List<String> directories = new ArrayList<>();
    private final List<File> fileList = new ArrayList<>();

    @Test
    void processShouldReturnDirectories() throws Exception {
        //given
        final String src = "src/test/projects/src-and-test/src/main/java";
        final String test = "src/test/projects/src-and-test/src/test/java";
        directories.add(src);
        directories.add(test);
        given(fileVisitor.preVisitDirectory(any(), any())).willReturn(FileVisitResult.CONTINUE);
        given(fileVisitor.visitFile(any(), any())).willReturn(FileVisitResult.CONTINUE);
        given(fileVisitor.postVisitDirectory(any(), any())).willReturn(FileVisitResult.CONTINUE);
        given(fileVisitor.getJavaFiles()).willReturn(fileList);
        //when
        List<File> files = sourceFileProvider.process(directories);
        //then
        then(fileVisitor).should()
                         .visitFile(eq(Paths.get(src, "test/nested/package-info.java")
                                            .toAbsolutePath()), any());
        then(fileVisitor).should()
                         .visitFile(eq(Paths.get(src, "test/nested/Src.java")
                                            .toAbsolutePath()), any());
        then(fileVisitor).should()
                         .visitFile(eq(Paths.get(src, "test/other/Imported.java")
                                            .toAbsolutePath()), any());
        then(fileVisitor).should()
                         .visitFile(eq(Paths.get(src, "test/other/Static.java")
                                            .toAbsolutePath()), any());
        then(fileVisitor).should()
                         .visitFile(eq(Paths.get(src, "test/other/StaticAll.java")
                                            .toAbsolutePath()), any());
        then(fileVisitor).should()
                         .visitFile(eq(Paths.get(test, "ignored.properties")
                                            .toAbsolutePath()), any());
        then(fileVisitor).should()
                         .visitFile(eq(Paths.get(test, "test/Tst.java")
                                            .toAbsolutePath()), any());
        assertThat(files).isEqualTo(fileList);
    }

    @Test
    @Disabled("no assert() or fail()")
    void processShouldLogExceptions() throws Exception {
        //given
        final String src = "src/test/projects/src-and-test/src/main/java";
        directories.add(src);
        IOException ex = new IOException();
        doThrow(ex).when(fileVisitor)
                   .preVisitDirectory(any(), any());
        given(digraphConfiguration.getLog()).willReturn(log);
        //when
        sourceFileProvider.process(directories);
        //then
        then(log).should()
                 .error(ex);
    }
}

package net.kemitix.pdg.maven;

import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DefaultSourceFileVisitor}.
 *
 * @author pcampbell
 */
public class DefaultSourceFileVisitorTest {

    private DefaultSourceFileVisitor fileVisitor;

    @Mock
    private BasicFileAttributes fileAttributes;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        fileVisitor = new DefaultSourceFileVisitor();
    }

    @Test
    public void visitFileAddFileWhenFileIsJava() throws Exception {
        //given
        val file = Paths.get("Main.java");
        //when
        fileVisitor.visitFile(file, fileAttributes);
        //then
        assertThat(fileVisitor.getJavaFiles()).contains(file.toFile());
    }

    @Test
    public void visitFileIgnoresFileWhenFileIsNotJava() throws Exception {
        //given
        val file = Paths.get("README.md");
        //when
        fileVisitor.visitFile(file, fileAttributes);
        //then
        assertThat(fileVisitor.getJavaFiles()).doesNotContain(file.toFile());
    }
}

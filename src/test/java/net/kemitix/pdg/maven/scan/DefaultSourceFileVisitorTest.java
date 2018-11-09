package net.kemitix.pdg.maven.scan;

import lombok.val;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import static org.mockito.Mockito.mock;

class DefaultSourceFileVisitorTest implements WithAssertions {

    private final BasicFileAttributes fileAttributes = mock(BasicFileAttributes.class);
    private final DefaultSourceFileVisitor fileVisitor = new DefaultSourceFileVisitor();

    @Test
    void visitFileAddFileWhenFileIsJava() throws Exception {
        //given
        val file = Paths.get("Main.java");
        //when
        fileVisitor.visitFile(file, fileAttributes);
        //then
        assertThat(fileVisitor.getJavaFiles()).contains(file.toFile());
    }

    @Test
    void visitFileIgnoresFileWhenFileIsNotJava() throws Exception {
        //given
        val file = Paths.get("README.md");
        //when
        fileVisitor.visitFile(file, fileAttributes);
        //then
        assertThat(fileVisitor.getJavaFiles()).doesNotContain(file.toFile());
    }
}

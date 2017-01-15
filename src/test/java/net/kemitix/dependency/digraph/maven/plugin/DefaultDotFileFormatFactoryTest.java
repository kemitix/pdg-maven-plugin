package net.kemitix.dependency.digraph.maven.plugin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

import net.kemitix.node.Node;
import net.kemitix.node.Nodes;

/**
 * Tests for {@link DefaultDotFileFormatFactory}.
 *
 * @author pcampbell
 */
public class DefaultDotFileFormatFactoryTest {

    private DefaultDotFileFormatFactory factory;

    @Mock
    private NodePathGenerator nodePathGenerator;

    private Node<PackageData> base;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        factory = new DefaultDotFileFormatFactory(nodePathGenerator);
        base = Nodes.unnamedRoot(null);
    }

    @Test
    public void createSimple() {
        //when
        DotFileFormat result = factory.create("simple", base);
        //then
        assertThat(result).isInstanceOf(DotFileFormatSimple.class);
    }

    @Test
    public void createNested() {
        //when
        DotFileFormat result = factory.create("nested", base);
        //then
        assertThat(result).isInstanceOf(DotFileFormatNested.class);
    }

    @Test
    public void createDefault() {
        //when
        DotFileFormat result = factory.create("default", base);
        //then
        assertThat(result).isInstanceOf(DotFileFormatNested.class);
    }
}

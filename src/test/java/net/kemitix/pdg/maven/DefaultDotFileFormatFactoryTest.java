package net.kemitix.pdg.maven;

import net.kemitix.node.Node;
import net.kemitix.node.Nodes;
import net.kemitix.pdg.maven.digraph.DotFileFormat;
import net.kemitix.pdg.maven.digraph.PackageData;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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

    @Mock
    private GraphFilter graphFilter;

    @Mock
    private TreeFilter treeFilter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        factory = new DefaultDotFileFormatFactory(nodePathGenerator, graphFilter, treeFilter);
        base = Nodes.unnamedRoot(null);
        /// default behaviour without any filters is to include
        given(graphFilter.filterNodes(any())).willReturn(true);
        given(treeFilter.filterTree(base)).willReturn(base);
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

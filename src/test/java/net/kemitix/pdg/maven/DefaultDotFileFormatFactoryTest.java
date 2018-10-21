package net.kemitix.pdg.maven;

import net.kemitix.node.Node;
import net.kemitix.node.Nodes;
import net.kemitix.pdg.maven.digraph.DotFileFormat;
import net.kemitix.pdg.maven.digraph.PackageData;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class DefaultDotFileFormatFactoryTest implements WithAssertions {

    private final NodePathGenerator nodePathGenerator = mock(NodePathGenerator.class);
    private final Node<PackageData> base = Nodes.unnamedRoot(null);
    private final GraphFilter graphFilter = mock(GraphFilter.class);
    private final TreeFilter treeFilter = mock(TreeFilter.class);

    private final DefaultDotFileFormatFactory factory =
            new DefaultDotFileFormatFactory(nodePathGenerator, graphFilter, treeFilter);

    @BeforeEach
    void setUp() throws Exception {
        /// default behaviour without any filters is to include
        given(graphFilter.filterNodes(any())).willReturn(true);
        given(treeFilter.filterTree(base)).willReturn(base);
    }

    @Test
    void createSimple() {
        //when
        final DotFileFormat result = factory.create("simple", base);
        //then
        assertThat(result).isInstanceOf(DotFileFormatSimple.class);
    }

    @Test
    void createNested() {
        //when
        final DotFileFormat result = factory.create("nested", base);
        //then
        assertThat(result).isInstanceOf(DotFileFormatNested.class);
    }

    @Test
    void createDefault() {
        //when
        final DotFileFormat result = factory.create("default", base);
        //then
        assertThat(result).isInstanceOf(DotFileFormatNested.class);
    }
}

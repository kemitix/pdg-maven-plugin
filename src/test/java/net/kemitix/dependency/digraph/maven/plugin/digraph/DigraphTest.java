package net.kemitix.dependency.digraph.maven.plugin.digraph;

import lombok.val;
import net.kemitix.dependency.digraph.maven.plugin.DotFileFormat;
import net.kemitix.dependency.digraph.maven.plugin.PackageData;
import net.kemitix.node.Node;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Tests for {@link DigraphTest}.
 */
public class DigraphTest {

    private Digraph digraph;

    @Mock
    private Node<PackageData> testNode;

    @Mock
    private Node<PackageData> alphaNode;

    @Mock
    private Node<PackageData> betaNode;

    @Mock
    private Node<PackageData> gammaNode;

    @Mock
    private DotFileFormat dotFileFormat;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        digraph = new Digraph(dotFileFormat);
    }

    @Test
    public void shouldGetDotFileFormat() {
        assertThat(digraph.getDotFileFormat()).isSameAs(dotFileFormat);
    }

    @Test
    public void shouldRenderDigraph() {
        //given
        val expected = "rendered digraph";
        given(dotFileFormat.render(digraph)).willReturn(expected);
        //then
        assertThat(digraph.render()).isEqualTo(expected);
    }

    @Test
    public void shouldConstructDigraphModel() {
        //given
        digraph.add(new PropertyElement("compound", "true", dotFileFormat));

        // define the appearance of nodes
        val nodeProperties = new NodeProperties(dotFileFormat);
        nodeProperties.add(new PropertyElement("shape", "box", dotFileFormat));
        digraph.add(nodeProperties);

        // subgraph alpha
        val subgraphAlpha = new Subgraph(alphaNode, "a", "alpha", dotFileFormat);
        val nodeElement = new NodeElement(testNode, "t", "test", dotFileFormat);
        subgraphAlpha.add(nodeElement);
        digraph.add(subgraphAlpha);

        // Node beta
        val nodeBeta = new NodeElement(betaNode, "b", "beta", dotFileFormat);
        subgraphAlpha.add(nodeBeta);

        // Node gamma
        val nodeGamma = new NodeElement(gammaNode, "gamma", "gamma", dotFileFormat);
        digraph.add(nodeGamma);

        // gamma -> beta
        val gammaUsesBeta = new EdgeElement(nodeGamma, nodeBeta, dotFileFormat);
        digraph.add(gammaUsesBeta);

        //then
        val digraphElements = digraph.getElements();
        assertThat(digraphElements).hasSize(5)
                                   .contains(nodeProperties, subgraphAlpha, nodeGamma, gammaUsesBeta)
                                   .doesNotContain(nodeBeta);

        val subgraphElements = subgraphAlpha.getElements();
        assertThat(subgraphElements).hasSize(2)
                                    .contains(nodeElement, nodeBeta);
    }
}

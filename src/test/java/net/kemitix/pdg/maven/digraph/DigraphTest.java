package net.kemitix.pdg.maven.digraph;

import lombok.val;
import net.kemitix.node.Node;
import net.kemitix.node.Nodes;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class DigraphTest implements WithAssertions {

    private final DotFileFormat dotFileFormat = mock(DotFileFormat.class);
    private final Digraph digraph = new Digraph(dotFileFormat);

    private final Node<PackageData> testNode = Nodes.unnamedRoot(new PackageData("test"));
    private final Node<PackageData> alphaNode = Nodes.unnamedRoot(new PackageData("alpha"));
    private final Node<PackageData> betaNode = Nodes.unnamedRoot(new PackageData("beta"));
    private final Node<PackageData> gammaNode = Nodes.unnamedRoot(new PackageData("gamma"));

    @Test
    void shouldGetDotFileFormat() {
        assertThat(digraph.getDotFileFormat()).isSameAs(dotFileFormat);
    }

    @Test
    void shouldRenderDigraph() {
        //given
        val expected = "rendered digraph";
        given(dotFileFormat.render(digraph)).willReturn(expected);
        //then
        assertThat(digraph.render()).isEqualTo(expected);
    }

    @Test
    void shouldConstructDigraphModel() {
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

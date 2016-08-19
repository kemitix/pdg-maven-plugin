package net.kemitix.dependency.digraph.maven.plugin.digraph;

import lombok.val;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.BDDMockito.given;

import net.kemitix.dependency.digraph.maven.plugin.DotFileFormat;
import net.kemitix.dependency.digraph.maven.plugin.PackageData;
import net.kemitix.node.Node;

/**
 * Tests for {@link DigraphTest}.
 */
public class DigraphTest {

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
    }

    @Test
    public void shouldGetDotFileFormat() {
        //given
        val digraph = new Digraph(dotFileFormat);
        //then
        assertThat(digraph.getFormat()).isSameAs(dotFileFormat);
    }

    @Test
    public void shouldRenderDigraph() {
        //given
        val digraph = new Digraph(dotFileFormat);
        val expected = "rendered digraph";
        given(dotFileFormat.render(digraph)).willReturn(expected);
        //then
        assertThat(digraph.render()).isEqualTo(expected);
    }

    @Test
    public void shouldConstructDigraphModel() {
        //given
        val digraph = new Digraph(dotFileFormat);
        digraph.add(new PropertyElement("compound", "true", dotFileFormat));

        // define the appearance of nodes
        val nodeProperties = new NodeProperties(dotFileFormat);
        nodeProperties.add(new PropertyElement("shape", "box", dotFileFormat));
        digraph.add(nodeProperties);

        // subgraph alpha
        val subgraphAlpha = new Subgraph(alphaNode, "a", "alpha",
                dotFileFormat);
        val nodeElement = new NodeElement(testNode, "t", "test", dotFileFormat);
        nodeElement.setStyle("dotted");
        subgraphAlpha.add(nodeElement);
        digraph.add(subgraphAlpha);

        // Node beta
        val nodeBeta = new NodeElement(betaNode, "b", "beta", dotFileFormat);
        subgraphAlpha.add(nodeBeta);

        // Node gamma
        val nodeGamma = new NodeElement(gammaNode, "gamma", "gamma",
                dotFileFormat);
        digraph.add(nodeGamma);

        // gamma -> beta
        val gammaUsesBeta = new EdgeElement(nodeGamma, nodeBeta, dotFileFormat);
        digraph.add(gammaUsesBeta);

        //then
        val digraphElements = digraph.getElements();
        Assert.assertThat(digraphElements.size(), is(5));
        Assert.assertThat(digraphElements, hasItem(nodeProperties));
        Assert.assertThat(digraphElements, hasItem(subgraphAlpha));
        Assert.assertThat(digraphElements, hasItem(nodeGamma));
        Assert.assertThat(digraphElements, hasItem(gammaUsesBeta));
        Assert.assertThat(digraphElements, not(hasItem(nodeBeta)));

        val subgraphElements = subgraphAlpha.getElements();
        Assert.assertThat(subgraphElements.size(), is(2));
        Assert.assertThat(subgraphElements, hasItem(nodeElement));
        Assert.assertThat(subgraphElements, hasItem(nodeBeta));
    }

}

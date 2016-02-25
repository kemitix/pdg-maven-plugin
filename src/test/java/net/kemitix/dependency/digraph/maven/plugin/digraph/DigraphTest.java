package net.kemitix.dependency.digraph.maven.plugin.digraph;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import java.util.List;

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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldConstructDigraphModel() {
        //given
        Digraph digraph = new Digraph();
        digraph.add(new PropertyElement("compound", "true"));

        // define the appearance of nodes
        final NodeProperties nodeProperties = new NodeProperties();
        nodeProperties.add(new PropertyElement("shape", "box"));
        digraph.add(nodeProperties);

        // subgraph alpha
        final Subgraph subgraphAlpha = new Subgraph(alphaNode, "a", "alpha");
        final NodeElement nodeElement = new NodeElement(testNode, "t", "test");
        nodeElement.setStyle("dotted");
        subgraphAlpha.add(nodeElement);
        digraph.add(subgraphAlpha);

        // Node beta
        final NodeElement nodeBeta = new NodeElement(betaNode, "b", "beta");
        subgraphAlpha.add(nodeBeta);

        // Node gamma
        final NodeElement nodeGamma = new NodeElement(gammaNode, "gamma",
                "gamma");
        digraph.add(nodeGamma);

        // gamma -> beta
        final EdgeElement gammaUsesBeta = new EdgeElement(nodeGamma, nodeBeta);
        digraph.add(gammaUsesBeta);

        //then
        final List<GraphElement> digraphElements = digraph.getElements();
        Assert.assertThat(digraphElements.size(), is(5));
        Assert.assertThat(digraphElements, hasItem(nodeProperties));
        Assert.assertThat(digraphElements, hasItem(subgraphAlpha));
        Assert.assertThat(digraphElements, hasItem(nodeGamma));
        Assert.assertThat(digraphElements, hasItem(gammaUsesBeta));
        Assert.assertThat(digraphElements, not(hasItem(nodeBeta)));

        final List<GraphElement> subgraphElements = subgraphAlpha.getElements();
        Assert.assertThat(subgraphElements.size(), is(2));
        Assert.assertThat(subgraphElements, hasItem(nodeElement));
        Assert.assertThat(subgraphElements, hasItem(nodeBeta));
    }

}

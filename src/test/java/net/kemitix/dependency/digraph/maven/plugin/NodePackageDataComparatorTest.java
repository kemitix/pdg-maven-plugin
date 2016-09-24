package net.kemitix.dependency.digraph.maven.plugin;

import lombok.val;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

import net.kemitix.node.Nodes;

/**
 * Tests for {@link NodePackageDataComparator}.
 *
 * @author pcampbell
 */
public class NodePackageDataComparatorTest {

    private NodePackageDataComparator comparator;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        comparator = new NodePackageDataComparator();
    }

    @Test
    public void compareAlphaComesBeforeBetaWhenAlphaBeta() throws Exception {
        //given
        val alphaPackage = PackageData.newInstance("alpha");
        val alphaNode = Nodes.unnamedRoot(alphaPackage);
        val betaPackage = PackageData.newInstance("beta");
        val betaNode = Nodes.unnamedRoot(betaPackage);
        //when
        val result = comparator.compare(alphaNode, betaNode);
        //then
        assertThat(result).isEqualTo(-1);
    }

    @Test
    public void compareAlphaComesBeforeBetaWhenBetaAlpha() throws Exception {
        //given
        val alphaPackage = PackageData.newInstance("alpha");
        val alphaNode = Nodes.unnamedRoot(alphaPackage);
        val betaPackage = PackageData.newInstance("beta");
        val betaNode = Nodes.unnamedRoot(betaPackage);
        //when
        val result = comparator.compare(betaNode, alphaNode);
        //then
        assertThat(result).isEqualTo(1);
    }

    @Test
    public void compareAlphaEqualsBetaWhenPackageNameNamesAreSame()
            throws Exception {
        //given
        val alphaPackage = PackageData.newInstance("alpha");
        val alphaNode = Nodes.unnamedRoot(alphaPackage);
        val betaPackage = PackageData.newInstance("alpha");
        val betaNode = Nodes.unnamedRoot(betaPackage);
        //when
        val result = comparator.compare(betaNode, alphaNode);
        //then
        assertThat(result).isEqualTo(0);
    }

    @Test
    public void compareShouldThrowExceptionWhenAlphaNodeIsEmpty() {
        //given
        exception.expect(IllegalStateException.class);
        exception.expectMessage("Node has no package data");
        val alphaNode = Nodes.unnamedRoot((PackageData) null);
        val betaPackage = PackageData.newInstance("beta");
        val betaNode = Nodes.unnamedRoot(betaPackage);
        //when
        comparator.compare(betaNode, alphaNode);
    }

    @Test
    public void compareShouldThrowExceptionWhenBetaNodeIsEmpty() {
        //given
        exception.expect(IllegalStateException.class);
        exception.expectMessage("Node has no package data");
        val alphaPackage = PackageData.newInstance("alpha");
        val alphaNode = Nodes.unnamedRoot(alphaPackage);
        val betaNode = Nodes.unnamedRoot((PackageData) null);
        //when
        comparator.compare(betaNode, alphaNode);
    }

    @Test
    public void compareShouldThrowExceptionWhenAlphaBetaNodesBothEmpty() {
        //given
        exception.expect(IllegalStateException.class);
        exception.expectMessage("Node has no package data");
        val alphaNode = Nodes.unnamedRoot((PackageData) null);
        val betaNode = Nodes.unnamedRoot((PackageData) null);
        //when
        comparator.compare(betaNode, alphaNode);
    }

}

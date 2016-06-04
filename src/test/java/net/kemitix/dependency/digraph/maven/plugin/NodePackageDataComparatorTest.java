package net.kemitix.dependency.digraph.maven.plugin;

import lombok.val;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

import net.kemitix.node.NodeItem;

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
        val alphaPackage = new PackageData("alpha");
        val alphaNode = new NodeItem<PackageData>(alphaPackage);
        val betaPackage = new PackageData("beta");
        val betaNode = new NodeItem<PackageData>(betaPackage);
        //when
        val result = comparator.compare(alphaNode, betaNode);
        //then
        assertThat(result).isEqualTo(-1);
    }

    @Test
    public void compareAlphaComesBeforeBetaWhenBetaAlpha() throws Exception {
        //given
        val alphaPackage = new PackageData("alpha");
        val alphaNode = new NodeItem<PackageData>(alphaPackage);
        val betaPackage = new PackageData("beta");
        val betaNode = new NodeItem<PackageData>(betaPackage);
        //when
        val result = comparator.compare(betaNode, alphaNode);
        //then
        assertThat(result).isEqualTo(1);
    }

    @Test
    public void compareAlphaEqualsBetaWhenPackageNameNamesAreSame()
            throws Exception {
        //given
        val alphaPackage = new PackageData("alpha");
        val alphaNode = new NodeItem<PackageData>(alphaPackage);
        val betaPackage = new PackageData("alpha");
        val betaNode = new NodeItem<PackageData>(betaPackage);
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
        val alphaNode = new NodeItem<PackageData>(null);
        val betaPackage = new PackageData("beta");
        val betaNode = new NodeItem<PackageData>(betaPackage);
        //when
        comparator.compare(betaNode, alphaNode);
    }

    @Test
    public void compareShouldThrowExceptionWhenBetaNodeIsEmpty() {
        //given
        exception.expect(IllegalStateException.class);
        exception.expectMessage("Node has no package data");
        val alphaPackage = new PackageData("alpha");
        val alphaNode = new NodeItem<PackageData>(alphaPackage);
        val betaNode = new NodeItem<PackageData>(null);
        //when
        comparator.compare(betaNode, alphaNode);
    }

    @Test
    public void compareShouldThrowExceptionWhenAlphaBetaNodesBothEmpty() {
        //given
        exception.expect(IllegalStateException.class);
        exception.expectMessage("Node has no package data");
        val alphaNode = new NodeItem<PackageData>(null);
        val betaNode = new NodeItem<PackageData>(null);
        //when
        comparator.compare(betaNode, alphaNode);
    }

}

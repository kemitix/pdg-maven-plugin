package net.kemitix.pdg.maven;

import lombok.val;
import net.kemitix.node.EmptyNodeException;
import net.kemitix.node.Nodes;
import net.kemitix.pdg.maven.digraph.PackageData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * Tests for {@link NodePackageDataComparator}.
 *
 * @author pcampbell
 */
public class NodePackageDataComparatorTest {

    private NodePackageDataComparator comparator;

    @BeforeEach
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
    public void compareAlphaEqualsBetaWhenPackageNameNamesAreSame() throws Exception {
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
        val alphaNode = Nodes.unnamedRoot((PackageData) null);
        val betaPackage = PackageData.newInstance("beta");
        val betaNode = Nodes.unnamedRoot(betaPackage);
        //then
        assertThatCode(() -> comparator.compare(betaNode, alphaNode))
                .isInstanceOf(EmptyNodeException.class);
    }

    @Test
    public void compareShouldThrowExceptionWhenBetaNodeIsEmpty() {
        //given
        val alphaPackage = PackageData.newInstance("alpha");
        val alphaNode = Nodes.unnamedRoot(alphaPackage);
        val betaNode = Nodes.unnamedRoot((PackageData) null);
        //when
        assertThatCode(() -> comparator.compare(betaNode, alphaNode))
                .isInstanceOf(EmptyNodeException.class);
    }

    @Test
    public void compareShouldThrowExceptionWhenAlphaBetaNodesBothEmpty() {
        //given
        val alphaNode = Nodes.unnamedRoot((PackageData) null);
        val betaNode = Nodes.unnamedRoot((PackageData) null);
        //when
        assertThatCode(() -> comparator.compare(betaNode, alphaNode))
                .isInstanceOf(EmptyNodeException.class);
    }

}

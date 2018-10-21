package net.kemitix.pdg.maven;

import lombok.val;
import net.kemitix.node.EmptyNodeException;
import net.kemitix.node.Nodes;
import net.kemitix.pdg.maven.digraph.PackageData;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

class NodePackageDataComparatorTest implements WithAssertions {

    private final NodePackageDataComparator comparator = new NodePackageDataComparator();

    @Test
    void compareAlphaComesBeforeBetaWhenAlphaBeta() throws Exception {
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
    void compareAlphaComesBeforeBetaWhenBetaAlpha() throws Exception {
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
    void compareAlphaEqualsBetaWhenPackageNameNamesAreSame() throws Exception {
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
    void compareShouldThrowExceptionWhenAlphaNodeIsEmpty() {
        //given
        val alphaNode = Nodes.unnamedRoot((PackageData) null);
        val betaPackage = PackageData.newInstance("beta");
        val betaNode = Nodes.unnamedRoot(betaPackage);
        //then
        assertThatCode(() -> comparator.compare(betaNode, alphaNode))
                .isInstanceOf(EmptyNodeException.class);
    }

    @Test
    void compareShouldThrowExceptionWhenBetaNodeIsEmpty() {
        //given
        val alphaPackage = PackageData.newInstance("alpha");
        val alphaNode = Nodes.unnamedRoot(alphaPackage);
        val betaNode = Nodes.unnamedRoot((PackageData) null);
        //when
        assertThatCode(() -> comparator.compare(betaNode, alphaNode))
                .isInstanceOf(EmptyNodeException.class);
    }

    @Test
    void compareShouldThrowExceptionWhenAlphaBetaNodesBothEmpty() {
        //given
        val alphaNode = Nodes.unnamedRoot((PackageData) null);
        val betaNode = Nodes.unnamedRoot((PackageData) null);
        //when
        assertThatCode(() -> comparator.compare(betaNode, alphaNode))
                .isInstanceOf(EmptyNodeException.class);
    }

}
